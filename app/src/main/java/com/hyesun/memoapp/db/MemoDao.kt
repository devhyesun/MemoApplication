package com.hyesun.memoapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hyesun.memoapp.db.model.Memo

@Dao
interface MemoDao {
    @Insert
    fun insert(memo: Memo): Long

    @Update
    fun update(memo: Memo): Int

    @Delete
    fun delete(memo: Memo): Int

    @Query("SELECT * FROM memo")
    fun getAllMemo(): LiveData<List<Memo>>

    @Query("SELECT * FROM memo WHERE id == :id")
    fun getMemo(id: Long): LiveData<Memo>
}