package com.hyesun.memoapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hyesun.memoapp.db.model.Image
import com.hyesun.memoapp.db.model.Memo

@Database(entities = [Memo::class, Image::class], version = 1)
abstract class DatabaseManager: RoomDatabase() {
    abstract fun memoDao(): MemoDao
    abstract fun imageDao(): ImageDao

    companion object {
        private const val DATABASE_NAME = "memo-db"

        @Volatile
        private var instance: DatabaseManager? = null

        fun getInstance(context: Context): DatabaseManager {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }

        private fun buildDatabase(context: Context): DatabaseManager {
            return Room.databaseBuilder(context, DatabaseManager::class.java, DATABASE_NAME)
                .build()
        }
    }
}