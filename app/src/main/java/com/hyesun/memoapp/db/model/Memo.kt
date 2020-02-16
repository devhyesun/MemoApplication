package com.hyesun.memoapp.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memo")
data class Memo(
    @ColumnInfo
    val title: String,
    @ColumnInfo
    val content: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var id: Long = 0
}