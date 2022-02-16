package com.example.sleeplog.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Sleep(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val createdAt: Date,
    val sleepDuration: Long,
    val sleepQuality: Int
)