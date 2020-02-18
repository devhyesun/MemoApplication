package com.hyesun.memoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hyesun.memoapp.db.MemoRepository

class MemoViewModelFactory(private val repository: MemoRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MemoViewModel(repository) as T
    }
}