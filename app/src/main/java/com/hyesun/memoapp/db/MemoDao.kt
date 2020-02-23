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

    @Transaction
    @Query("DELETE FROM memo WHERE id = :id")
    fun delete(id: Long): Int

    @Query("SELECT * FROM memo ORDER BY date DESC")
    fun getAllMemo(): LiveData<List<Memo>>

    @Query("SELECT * FROM memo WHERE id = :id")
    fun getMemo(id: Long): LiveData<Memo>
}