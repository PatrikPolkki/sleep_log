package com.example.sleeplog.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Class for date and time formatter functions
 */
class DateAndTimeFormatter @Inject constructor() {

    fun stringToDate(date: String): Date? {
        return SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).parse(date)
    }

    fun longToDate(selectedDate: Long?): String {
        val date = selectedDate?.let { Date(it) }
        val format = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
        return if (date != null) {
            format.format(date)
        } else {
            format.format(getToday())
        }
    }

    fun dateToWeekday(date: Date): String {
        return SimpleDateFormat("EEEE", Locale.ENGLISH).format(date)
    }

    fun dateShort(date: Date): String {
        return SimpleDateFormat("dd/MM/yy", Locale.ENGLISH).format(date)
    }

    fun dateMedium(date: Date): String {
        return SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).format(date)
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