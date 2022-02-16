package com.example.sleeplog.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DateAndTimeFormatter @Inject constructor() {

    fun stringToDate(date: String): Date? {
        return DateFormat.getDateInstance(DateFormat.MEDIUM).parse(date)
    }

    fun dateToWeekday(date: Date): String {
        return SimpleDateFormat("EEEE", Locale.getDefault()).format(date)
    }

    fun dateShort(date: Date): String {
        return DateFormat.getDateInstance(DateFormat.SHORT).format(date)
    }

    fun dateMedium(date: Date): String {
        return DateFormat.getDateInstance(DateFormat.MEDIUM).format(date)
    }

    fun getTime(hour: Int, minute: Int): Long {
        val hours = TimeUnit.HOURS.toMillis(hour.toLong())
        val minutes = TimeUnit.MINUTES.toMillis(minute.toLong())

        return hours.plus(minutes)
    }

    fun getHours(duration: Long): Int {
        return TimeUnit.MILLISECONDS.toHours(duration).toInt()
    }

    fun getMinutes(duration: Long): Int {
        return (TimeUnit.MILLISECONDS.toMinutes(duration) % TimeUnit.HOURS.toMinutes(1)).toInt()
    }

    fun getToday(): Date {
        return Calendar.getInstance().time
    }
}