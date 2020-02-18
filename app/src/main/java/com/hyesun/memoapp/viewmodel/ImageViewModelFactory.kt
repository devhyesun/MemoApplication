package com.hyesun.memoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hyesun.memoapp.db.ImageRepository

class ImageViewModelFactory(private val repository: ImageRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ImageViewModel(repository) as T
    }
}