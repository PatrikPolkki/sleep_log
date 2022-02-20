package com.example.sleeplog.utils

import android.util.Log
import android.widget.NumberPicker
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.sleeplog.R
import com.example.sleeplog.database.Sleep
import com.google.android.material.chip.ChipGroup
import java.util.*
import javax.inject.Inject

/**
 * Binding adapters for various views
 * BindingAdapters wrapped to inside a class to make constructor injection possible
 */
class BindingAdapters @Inject constructor(private val dateFormatter: DateAndTimeFormatter) {
    @BindingAdapter("sleepDay")
    fun bindSleepDay(view: TextView, date: Date) {
        view.text = dateFormatter.dateToWeekday(date)
    }

    @BindingAdapter("sleepDuration")
    fun bindSleepDuration(view: TextView, duration: Long?) {
        val hours = duration?.let { dateFormatter.getHours(it) }
        val minutes = duration?.let { dateFormatter.getMinutes(it) }

        if (hours != null && minutes != null) {
            view.text = view.context.getString(R.string.duration_time, hours, minutes)
        } else {
            view.text = view.context.getString(R.string.empty_duration)
        }
    }

    @BindingAdapter("sleepDate")
    fun bindSleepDate(view: TextView, date: Date) {
        view.text = dateFormatter.dateShort(date)
    }

    @BindingAdapter("dialogDate")
    fun bindDialogDate(view: TextView, sleep: Sleep?) {
        val value = sleep?.let { dateFormatter.dateMedium(it.createdAt) }
        view.text = value ?: dateFormatter.dateMedium(dateFormatter.getToday())
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

    @BindingAdapter("pickerHourValue")
    fun bindPickerHourValue(view: NumberPicker, sleep: Sleep?) {
        val value = sleep?.let { dateFormatter.getHours(it.sleepDuration) }
        Log.d("hour", sleep.toString())
        view.minValue = 3
        view.maxValue = 12

        view.value = value ?: 3
    }

    @BindingAdapter("pickerMinuteValue")
    fun bindPickerMinuteValue(view: NumberPicker, sleep: Sleep?) {
        val value = sleep?.let { dateFormatter.getMinutes(it.sleepDuration) }
        view.minValue = 0
        view.maxValue = 59
        view.value = value ?: 0
    }

    @BindingAdapter("dialogChipGroup")
    fun bindDialogChipGroup(view: ChipGroup, sleep: Sleep?) {
        if (sleep != null) {
            when (sleep.sleepQuality) {
                1 -> {
                    view.check(R.id.chip_1)
                }
                2 -> {
                    view.check(R.id.chip_2)
                }
                3 -> {
                    view.check(R.id.chip_3)
                }
                4 -> {
                    view.check(R.id.chip_4)
                }
                5 -> {
                    view.check(R.id.chip_5)
                }
            }
        } else {
            view.check(R.id.chip_3)
        }
    }
}