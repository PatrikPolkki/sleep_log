package com.example.sleeplog.ui

import java.util.*

interface CellClickListener {
    fun onCellClickListener(duration: Long, quality: Int, date: Date, id: Long)
}