package com.hyesun.memoapp.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun takePicture(activity: Activity, requestCode: Int): String? {
        var photoURI: Uri? = null
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity.packageManager)?.also {
                val photoFile = try {
                    createImageFile(activity)
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    null
                }
                photoFile?.also {
                    photoURI = FileProvider.getUriForFile(
                        activity,
                        "com.hyesun.memoapp",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    activity.startActivityForResult(takePictureIntent, requestCode)
                }

            }
        }

        return photoURI.toString()
    }

    fun goToGallery(activity: Activity, requestCode: Int) {
        activity.startActivityForResult(Intent(Intent.ACTION_GET_CONTENT).apply {
            type = MediaStore.Images.Media.CONTENT_TYPE
        }, requestCode)
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(activity: Activity): File {
        val timeStamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        Log.i("_hs", "timeStamp : $timeStamp")
        val pictureDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            timeStamp,
            ".jpg",
            pictureDir
        )
    }
}