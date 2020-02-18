package com.hyesun.memoapp.db

import com.hyesun.memoapp.db.model.Image
import io.reactivex.Single

class ImageRepository private constructor(private val imageDao: ImageDao) {
    companion object {
        @Volatile
        private var instance: ImageRepository? = null

        fun getInstance(imageDao: ImageDao) =
            instance ?: synchronized(this) {
                instance ?: ImageRepository(imageDao).also {
                    instance = it
                }
            }
    }

    fun insert(image: Image) = Single.fromCallable { imageDao.insert(image) }

    fun update(image: Image) = Single.fromCallable { imageDao.update(image) }

    fun delete(image: Image) = Single.fromCallable { imageDao.delete(image) }

    fun getImageList(memoId: Long) = imageDao.getImageList(memoId)
}