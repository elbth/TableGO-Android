package com.example.tablego.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tablego.data.local.dao.ReviewDao
import com.example.tablego.data.local.entity.ReviewEntity

@Database(
    entities = [EventEntity::class, ReviewEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun reviewDao(): ReviewDao
    abstract fun eventDao(): EventDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration() // <- wipes DB if schema changes (good for dev)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
