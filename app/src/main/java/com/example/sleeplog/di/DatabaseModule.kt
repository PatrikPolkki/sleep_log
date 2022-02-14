package com.example.sleeplog.di

import android.content.Context
import androidx.room.Room
import com.example.sleeplog.database.AppDatabase
import com.example.sleeplog.database.SleepDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext app: Context): AppDatabase {
        return Room.databaseBuilder(
            app, AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideSleepDao(database: AppDatabase): SleepDao {
        return database.sleepDao()
    }

}