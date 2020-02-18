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
    private val compositeDisposable = CompositeDisposable()

    val allMemoList = memoRepository.getAllMemo()

    fun insert(memo: Memo) {
        compositeDisposable.add(
            memoRepository.insert(memo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { i -> Log.i("_hs", "image insert $i success") },
                    { throwable -> throwable.printStackTrace() }
                )
        )

    }

    fun update(memo: Memo) {
        compositeDisposable.add(
            memoRepository.update(memo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { i -> Log.i("_hs", "image update $i success") },
                    { throwable -> throwable.printStackTrace() }
                )
        )
    }

    fun delete(memo: Memo) {
        compositeDisposable.add(
            memoRepository.delete(memo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { i -> Log.i("_hs", "image delete $i success") },
                    { throwable -> throwable.printStackTrace() }
                )
        )
    }

    fun memo(memoId: Long) = memoRepository.getMemo(memoId)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}