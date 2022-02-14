package com.example.sleeplog.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Sleep(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sleepDuration: Float,
    val sleepQuality: String
)