package com.example.sleeplog.ui.dialog

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.sleeplog.R
import com.example.sleeplog.databinding.SleepDialogBinding
import com.example.sleeplog.ui.MainViewModel
import com.example.sleeplog.utils.DateAndTimeFormatter
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DialogFragment : DialogFragment() {

    @Inject
    lateinit var dateFormatter: DateAndTimeFormatter

    lateinit var binding: SleepDialogBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SleepDialogBinding.inflate(layoutInflater)

        binding.apply {
            dialogViewModel = viewModel
            lifecycleOwner = this@DialogFragment
        }

        initializeDialog()

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
        binding.addButton.setOnClickListener {
            addSleepDialog()
        }

        return binding.root
    }

    private fun initializeDialog() {
        binding.dateButton.setOnClickListener {
            dialogDatePicker(binding.dateText)
        }
    }

    private fun dialogDatePicker(view: TextView) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).setTitleText("Select Date")
            .build()

        activity?.let { datePicker.show(it.supportFragmentManager, null) }

        datePicker.addOnPositiveButtonClickListener {
            view.text = datePicker.headerText
        }
        datePicker.addOnNegativeButtonClickListener {
        }
    }

    private fun addSleepDialog() {
        lifecycleScope.launch {
            val id = viewModel.sleepItem.value?.id
            val date = dateFormatter.stringToDate(binding.dateText.text.toString())
            val quality = checkedChip()
            val duration =
                dateFormatter.getTime(binding.hoursPicker.value, binding.minutesPicker.value)
            val existingDAte = date?.let { viewModel.checkDate(it) }

            Log.d("ARVO", id.toString())

            if (quality != null && date != null) {
                if (id == null) {
                    if (existingDAte == null) {
                        viewModel.addSleep(date, duration, quality)
                        dismiss()
                    } else {
                        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                        builder.apply {
                            setTitle("Replace Existing Sleep Log")
                            setMessage("This sleep log for this date exists. Are you sure you want to replace it?")
                            setNegativeButton("Cancel") { _, _ ->
                            }
                            setPositiveButton("Replace") { _, _ ->
                                viewModel.updateSleep(duration, quality, date, existingDAte)
                                dismiss()
                            }
                            show()
                        }
                    }
                } else {
                    viewModel.updateSleep(duration, quality, date, id)
                    dismiss()
                }
            }
        }
    }

    private fun checkedChip(): Int? {
        return when (binding.chipGroup.checkedChipId) {
            R.id.chip_1 -> {
                1
            }
            R.id.chip_2 -> {
                2
            }
            R.id.chip_3 -> {
                3
            }
            R.id.chip_4 -> {
                4
            }
            R.id.chip_5 -> {
                5
            }
            else -> {
                return null
            }
        }
    }
}