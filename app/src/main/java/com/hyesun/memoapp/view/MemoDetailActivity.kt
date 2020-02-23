package com.hyesun.memoapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hyesun.memoapp.R
import com.hyesun.memoapp.adapter.ImageAdapter
import com.hyesun.memoapp.util.InjectorUtils
import com.hyesun.memoapp.viewmodel.ImageViewModel
import com.hyesun.memoapp.viewmodel.MemoViewModel
import kotlinx.android.synthetic.main.activity_memo_detail.*
import kotlinx.android.synthetic.main.row_memo.tvContent
import kotlinx.android.synthetic.main.row_memo.tvTitle

class MemoDetailActivity : AppCompatActivity() {
    var memoViewModel: MemoViewModel? = null
    var imageViewModel: ImageViewModel? = null

    var memoId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_detail)

        memoId = intent.getLongExtra("memoId", -1)

        memoViewModel = ViewModelProvider(
            this,
            InjectorUtils.provideMemoViewModelFactory(this)
        ).get(MemoViewModel::class.java)

        imageViewModel = ViewModelProvider(
            this,
            InjectorUtils.provideImageViewModelFactory(this)
        ).get(ImageViewModel::class.java)

        if (memoId > -1) {
            val adapter = ImageAdapter(this, false)
            rvImageList.layoutManager = LinearLayoutManager(this)
            rvImageList.adapter = adapter

            memoViewModel?.memo(memoId)?.observe(this, Observer {
                if (it != null) {
                    tvTitle.text = it.title
                    tvContent.text = it.content
                }
            })

            imageViewModel?.imageList(memoId)?.observe(this, Observer {
                rvImageList.visibility = if (it.isNotEmpty()) {
                    adapter.setImagePathList(it)
                    View.VISIBLE
                } else {
                    View.GONE
                }
            })
        }
    }
}
