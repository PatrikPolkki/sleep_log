package com.example.sleeplog.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.*

/**
 * Queries for sleep database
 */
@Dao
interface SleepDao {
    @Insert
    suspend fun insertSleep(sleep: Sleep): Long

    @Query("DELETE FROM Sleep WHERE id = :id")
    suspend fun deleteSleep(id: Long)

    @Query("SELECT * FROM Sleep ORDER BY createdAt DESC")
    fun getAll(): Flow<List<Sleep>>

    @Query("UPDATE Sleep SET sleepDuration = :duration, sleepQuality = :quality, createdAt = :date WHERE id = :id")
    suspend fun updateSleep(duration: Long, quality: Int, date: Date, id: Long)

    @Query("SELECT id FROM Sleep WHERE createdAt = :date")
    suspend fun checkIfExists(date: Date): Long?

    @Query("SELECT AVG(sleepDuration) FROM Sleep")
    fun getAverageSleep(): Flow<Long?>

    @Query("SELECT sleepDuration FROM Sleep ORDER BY createdAt DESC")
    fun getLatestSleep(): Flow<Long?>
}