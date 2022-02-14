package com.example.sleeplog.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Sleep::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sleepDao(): SleepDao
}