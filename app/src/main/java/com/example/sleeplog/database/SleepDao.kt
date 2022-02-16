package com.example.sleeplog.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface SleepDao {
    @Insert
    suspend fun insertSleep(sleep: Sleep): Long

    @Query("DELETE FROM Sleep WHERE id = :id")
    suspend fun deleteSleep(id: Long)

    @Query("SELECT * FROM Sleep ORDER BY createdAt ASC")
    fun getAll(): Flow<List<Sleep>>

    @Query("UPDATE Sleep SET sleepDuration = :duration WHERE id = :id")
    suspend fun updateSleepDuration(duration: Long, id: Long)

    @Query("UPDATE Sleep SET sleepQuality = :quality WHERE id = :id")
    suspend fun updateSleepQuality(quality: Int, id: Long)

    @Query("UPDATE Sleep SET sleepDuration = :duration AND sleepQuality = :quality WHERE createdAt = :date")
    suspend fun replaceAll(duration: Long, quality: Int, date: Date)

    @Query("UPDATE Sleep SET sleepDuration = :duration WHERE createdAt = :date")
    suspend fun replaceSleepDuration(duration: Long, date: Date)

    @Query("UPDATE Sleep SET sleepDuration = :quality WHERE createdAt = :date")
    suspend fun replaceSleepQuality(quality: Int, date: Date)

    @Query("SELECT EXISTS(SELECT id FROM Sleep WHERE createdAt = :date)")
    suspend fun checkIfExists(date: Date): Long?
}