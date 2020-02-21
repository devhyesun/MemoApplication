package com.hyesun.memoapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hyesun.memoapp.db.model.Image

@Dao
interface ImageDao {
    @Insert
    fun insert(image: Image): Long

    @Update
    fun update(image: Image): Int

    @Delete
    fun delete(image: Image): Int

    @Query("SELECT path FROM image WHERE memo_id == :memoId ORDER BY add_time LIMIT 1")
    fun getThumbnail(memoId: Long): LiveData<String>

    @Query("SELECT path FROM image WHERE memo_id == :memoId ORDER BY add_time DESC")
    fun getImageList(memoId: Long): LiveData<List<String>>
}