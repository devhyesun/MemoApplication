package com.hyesun.memoapp.db.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(tableName = "image",
    foreignKeys = [ForeignKey(
        entity = Memo::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("memo_id"),
        onDelete = CASCADE
    )],
    indices = [Index("memo_id")]
)
data class Image(
    @ColumnInfo
    val path: String,
    @ColumnInfo(name = "memo_id")
    val memoId: Long
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "image_id")
    var imageId: Long = 0
    @ColumnInfo(name = "add_time")
    var addTime: Long = System.currentTimeMillis()
}