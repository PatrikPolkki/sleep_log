package com.example.sleeplog.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SleepDao {
    @Insert
    suspend fun insertSleep(sleep: Sleep): Long

    @Query("DELETE FROM Sleep WHERE id = :id")
    suspend fun deleteSleep(id: Long)

    @Query("SELECT * FROM Sleep")
    fun getAll(): Flow<List<Sleep>>

    @Query("UPDATE Sleep SET sleepDuration = :duration WHERE id = :id")
    suspend fun updateSleepDuration(duration: Float, id: Long)

    @Query("UPDATE Sleep SET sleepQuality = :quality WHERE id = :id")
    suspend fun updateSleepQuality(quality: Int, id: Long)
}