package com.hyesun.memoapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.hyesun.memoapp.db.MemoRepository
import com.hyesun.memoapp.db.model.Memo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MemoViewModel internal constructor(
    private val memoRepository: MemoRepository
) : ViewModel() {
    val compositeDisposable = CompositeDisposable()

    val allMemoList = memoRepository.getAllMemo()

    fun insert(memo: Memo) =
        memoRepository.insert(memo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    fun update(memo: Memo) =
        memoRepository.update(memo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun delete(id: Long) = memoRepository.delete(id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun memo(memoId: Long) = memoRepository.getMemo(memoId)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}