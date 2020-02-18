package com.hyesun.memoapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.hyesun.memoapp.db.ImageRepository
import com.hyesun.memoapp.db.model.Image
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ImageViewModel internal constructor(
    private val imageRepository: ImageRepository): ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    fun insert(image: Image) {
        compositeDisposable.add(
            imageRepository.insert(image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { i -> Log.i("_hs", "memo insert $i success") },
                    { throwable -> throwable.printStackTrace() }
                )
        )
    }

    fun update(image: Image) {
        compositeDisposable.add(
            imageRepository.update(image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { i -> Log.i("_hs", "memo update $i success") },
                    { throwable -> throwable.printStackTrace() }
                )
        )
    }

    fun delete(image: Image) {
        compositeDisposable.add(
            Single.just(imageRepository.delete(image))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { i -> Log.i("_hs", "memo delete $i success") },
                    { throwable -> throwable.printStackTrace() }
                )
        )
    }

    fun thumbnailImage(memoId: Long) = imageList(memoId).value?.get(0)
    fun imageList(memoId: Long) = imageRepository.getImageList(memoId)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}