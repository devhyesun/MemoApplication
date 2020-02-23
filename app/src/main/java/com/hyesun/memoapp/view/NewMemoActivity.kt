package com.hyesun.memoapp.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hyesun.memoapp.R
import com.hyesun.memoapp.adapter.ImageAdapter
import com.hyesun.memoapp.db.model.Image
import com.hyesun.memoapp.db.model.Memo
import com.hyesun.memoapp.listener.OnDeleteListener
import com.hyesun.memoapp.util.InjectorUtils
import com.hyesun.memoapp.util.Utils
import com.hyesun.memoapp.viewmodel.ImageViewModel
import com.hyesun.memoapp.viewmodel.MemoViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_new_memo.*
import java.net.URL


class NewMemoActivity : AppCompatActivity(), View.OnClickListener, OnDeleteListener {
    private val requestStoragePermission = 0x0010
    private val requestCameraPermission = 0x0011

    private val requestCamera = 0x0001
    private val requestGallery = 0x0002

    private var memoViewModel: MemoViewModel? = null
    private var imageViewModel: ImageViewModel? = null

    private var imagePath: String? = null
    private val imagePathLiveData = MutableLiveData<ArrayList<String>>()
    private var imagePathList = ArrayList<String>()

    private var memo: Memo? = null
    private var memoId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_memo)

        memoViewModel = ViewModelProvider(
            this,
            InjectorUtils.provideMemoViewModelFactory(this)
        ).get(MemoViewModel::class.java)

        imageViewModel = ViewModelProvider(
            this,
            InjectorUtils.provideImageViewModelFactory(this)
        ).get(ImageViewModel::class.java)

        val adapter = ImageAdapter(this, true)
        adapter.setOnDeleteListener(this)
        rvImageList.layoutManager = LinearLayoutManager(this)
        rvImageList.adapter = adapter

        imagePathLiveData.observe(this, Observer {
            rvImageList.visibility = if (it.size > 0) {
                View.VISIBLE
            } else {
                View.GONE
            }
            adapter.setImagePathList(it)
        })

        memoId = intent.getLongExtra("memoId", -1)

        if(memoId > - 1) {
            memoViewModel?.memo(memoId)?.observe(this, Observer {
                memo = it
                tietTitle.setText(it.title)
                tietContent.setText(it.content)
            })

            imageViewModel?.imageList(memoId)?.observe(this, Observer {
                imagePathLiveData.value = it as ArrayList<String>
                imagePathList.addAll(it)
            })
        }

        btnAddImage.setOnClickListener(this)
        btnCancel.setOnClickListener(this)
        btnSave.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAddImage -> {
                if (Utils.checkCameraPermission(this)
                    && Utils.checkStoragePermission(this)
                ) {
                    addImage()
                } else {
                    Utils.requestCameraPermission(this, requestCameraPermission)
                }
            }

            R.id.btnCancel -> {
                finish()
            }

            R.id.btnSave -> {
                val title = tietTitle.text.toString()
                val content = tietContent.text.toString()

                memoViewModel?.let { viewModel ->
                    viewModel.compositeDisposable.add(
                        memo?.let {
                            viewModel.update(it)
                                .subscribe(
                                    { i ->
                                        Log.i("_hs", "memo $i update")
                                    },
                                    { exception -> exception.printStackTrace() }
                                )
                        } ?: let {
                            viewModel.insert(Memo(title, content))
                                .subscribe(
                                    { id ->
                                        for (path in imagePathList) {
                                            imageViewModel?.insert(Image(path, id))
                                        }
                                    },
                                    { exception -> exception.printStackTrace() }
                                )
                        }
                    )
                }

                imageViewModel?.let { viewModel ->
                    memo?.let {
                        viewModel.compositeDisposable.add(
                            viewModel.delete(memoId)
                                .subscribe(
                                    {
                                        for (path in imagePathList) {
                                            imageViewModel?.insert(Image(path, memoId))
                                        }
                                    },
                                    { exception -> exception.printStackTrace() }
                                )
                        )
                    }
                }

                finish()
            }
        }
    }

    private fun addImage() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.str_add_picture))
            .setItems(
                resources.getStringArray(R.array.str_ways_to_add_picture)
            ) { dialog, which ->
                when (which) {
                    0 -> {  // from camera
                        imagePath = Utils.takePicture(this, requestCamera)
                    }
                    1 -> {  // from gallery
                        Utils.goToGallery(this, requestGallery)
                    }
                    2 -> {  // from url
                        showFromUrlDialog()
                    }
                }
            }
            .show()
    }

    private fun showFromUrlDialog() {
        val editText = EditText(this)

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.str_add_picture))
            .setMessage(getString(R.string.str_input_url))
            .setView(editText)
            .setPositiveButton(getString(R.string.str_ok)) { dialog, _ ->
                Single.fromCallable {
                    URL(editText.text.toString())
                        .openConnection()
                        .getHeaderField("Content-Type")
                        .startsWith("image/")
                }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it) {
                            imagePathList.add(editText.text.toString())
                            imagePathLiveData.value = imagePathList
                        } else {
                            Toast.makeText(
                                this,
                                getString(R.string.str_invalid_url),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                        { ex ->
                            Toast.makeText(
                                this,
                                getString(R.string.str_invalid_url),
                                Toast.LENGTH_SHORT
                            ).show()

                            ex.printStackTrace()
                        })
            }
            .setNegativeButton(getString(R.string.str_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDelete(position: Int) {
        if (imagePathList.size > 0) {
            imagePathList.removeAt(position)
            imagePathLiveData.value = imagePathList
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            requestCamera -> {
                if (resultCode == Activity.RESULT_OK) {
                    imagePath?.let { imagePath ->
                        imagePathList.add(imagePath)
                        imagePathLiveData.value = imagePathList
                    }
                }
            }
            requestGallery -> {
                if (resultCode == Activity.RESULT_OK) {
                    val uri = data?.data

                    uri?.let {
                        contentResolver.takePersistableUriPermission(
                            it,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        )
                    }

                    imagePathList.add(uri.toString())
                    imagePathLiveData.value = imagePathList
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            requestCameraPermission -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    Utils.requestStoragePermission(this, requestStoragePermission)
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.CAMERA
                        )
                    ) {
                        showRequirePermissionToast()
                    } else {
                        showRequirePermissionToast()
                    }
                }
            }
            requestStoragePermission -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    addImage()
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    ) {
                        showRequirePermissionToast()
                    } else {
                        showRequirePermissionToast()
                    }
                }
            }
        }
    }

    private fun showRequirePermissionToast() {
        Toast.makeText(
            this, getString(R.string.str_require_storage_permission),
            Toast.LENGTH_SHORT
        ).show()
    }
}

