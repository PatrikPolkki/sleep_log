package com.example.sleeplog.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.sleeplog.R
import java.util.*
import javax.inject.Inject

class BindingAdapters @Inject constructor(var dateAndTimeFormatter: DateAndTimeFormatter) {
    @BindingAdapter("sleepDay")
    fun bindSleepDay(view: TextView, date: Date) {
        view.text = dateAndTimeFormatter.dateToWeekday(date)
    }

    @BindingAdapter("sleepDuration")
    fun bindSleepDuration(view: TextView, duration: Long) {
        val hours = dateAndTimeFormatter.getHours(duration)
        val minutes = dateAndTimeFormatter.getMinutes(duration)

        view.text = view.context.getString(R.string.duration_time, hours, minutes)
    }


    @BindingAdapter("sleepDate")
    fun bindSleepDate(view: TextView, date: Date) {
        view.text = dateAndTimeFormatter.dateShort(date)
    }

    @BindingAdapter("dialogDate")
    fun bindDialogDate(view: TextView, date: Date) {
        view.text = dateAndTimeFormatter.dateMedium(date)
    }

    @BindingAdapter("sleepQuality")
    fun bindSleepQuality(view: TextView, quality: Int) {
        when (quality) {
            1 -> {
                view.text = view.context.getString(R.string.chip_poor)
            }
            2 -> {
                view.text = view.context.getString(R.string.chip_fair)
            }
            3 -> {
                view.text = view.context.getString(R.string.chip_average)
            }
            4 -> {
                view.text = view.context.getString(R.string.chip_good)
            }
            5 -> {
                view.text = view.context.getString(R.string.chip_very_good)
            }
        }
    }
}