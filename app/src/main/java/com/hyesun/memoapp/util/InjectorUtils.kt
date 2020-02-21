package com.hyesun.memoapp.util

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.hyesun.memoapp.db.DatabaseManager
import com.hyesun.memoapp.db.ImageRepository
import com.hyesun.memoapp.db.MemoRepository
import com.hyesun.memoapp.viewmodel.ImageViewModelFactory
import com.hyesun.memoapp.viewmodel.MemoViewModelFactory

object InjectorUtils {
    private fun getMemoRepository(context: Context): MemoRepository {
        return MemoRepository.getInstance(
            DatabaseManager.getInstance(context.applicationContext).memoDao()
        )
    }

    private fun getImageRepository(context: Context): ImageRepository {
        return ImageRepository.getInstance(
            DatabaseManager.getInstance(context.applicationContext).imageDao()
        )
    }

    fun provideMemoViewModelFactory(context: Context): MemoViewModelFactory {
        val repository = getMemoRepository(context)
        return MemoViewModelFactory(repository)
    }

    fun provideImageViewModelFactory(context: Context): ImageViewModelFactory {
        val repository = getImageRepository(context)
        return ImageViewModelFactory(repository)
    }
}