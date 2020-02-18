package com.hyesun.memoapp.db

import com.hyesun.memoapp.db.model.Memo
import io.reactivex.Single

class MemoRepository private constructor(private val memoDao: MemoDao) {
    companion object {
        @Volatile
        private var instance: MemoRepository? = null

        fun getInstance(memoDao: MemoDao) =
            instance ?: synchronized(this) {
                instance ?: MemoRepository(memoDao).also {
                    instance = it
                }
            }
    }

    fun insert(memo: Memo) = Single.fromCallable { memoDao.insert(memo) }

    fun update(memo: Memo) = Single.fromCallable { memoDao.update(memo) }

    fun delete(memo: Memo) = Single.fromCallable { memoDao.delete(memo) }

    fun getMemo(memoId: Long) = memoDao.getMemo(memoId)

    fun getAllMemo() = memoDao.getAllMemo()
}