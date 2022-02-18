package com.example.sleeplog.ui

import com.example.sleeplog.database.Sleep

interface CellClickListener {
    fun onCellClickListener(sleepItem: Sleep)
}