package com.example.tablego.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tablego.data.local.dao.ReviewDao
import com.example.tablego.data.local.entity.ReviewEntity

@Database(
    entities = [ReviewEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun reviewDao(): ReviewDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tablego.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
