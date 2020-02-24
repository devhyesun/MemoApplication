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
    @ColumnInfo(name = "date")
    var date: Long = System.currentTimeMillis()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Memo

        if (title != other.title) return false
        if (content != other.content) return false
        if (id != other.id) return false
        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + date.hashCode()
        return result
    }


}