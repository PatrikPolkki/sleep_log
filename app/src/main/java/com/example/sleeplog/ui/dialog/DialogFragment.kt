package com.example.sleeplog.ui.dialog

import android.app.AlertDialog
import android.os.Bundle
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

        // initialize databinding
        binding.apply {
            dialogViewModel = viewModel
            lifecycleOwner = this@DialogFragment
        }

        initializeDialogButtons()

        return binding.root
    }

    private fun initializeDialogButtons() {
        binding.dateButton.setOnClickListener {
            dialogDatePicker(binding.dateText)
        }
        binding.cancelButton.setOnClickListener {
            dismiss()
        }
        binding.addButton.setOnClickListener {
            addSleepDialog()
        }
    }

    /**
     * Opens datepicker dialog
     * takes date textview as parameter
     * set selected date to date textview using [dateFormatter]
     */
    private fun dialogDatePicker(view: TextView) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).setTitleText("Select Date")
            .build()

        activity?.let { datePicker.show(it.supportFragmentManager, null) }

        datePicker.addOnPositiveButtonClickListener {
            view.text = dateFormatter.longToDate(datePicker.selection)
        }
        datePicker.addOnNegativeButtonClickListener {
        }
    }

    /**
     * Add new sleep record to database
     * updates existing sleep record if it is passed to [viewModel]
     */
    private fun addSleepDialog() {
        lifecycleScope.launch {
            val sleepItem = viewModel.sleepItem.value
            // format date textview value to date
            val date = dateFormatter.stringToDate(binding.dateText.text.toString())
            // check which chip is checked
            val quality = checkedChip()
            // convert timepicker values to Long
            val duration =
                dateFormatter.getTime(binding.hoursPicker.value, binding.minutesPicker.value)

            // check if sleep record exist in given date
            val dateExists = date?.let { viewModel.checkDate(it) }

            if (date != null) {
                if (sleepItem == null) {
                    if (dateExists == null) {
                        viewModel.addSleep(date, duration, quality)
                        dismiss()
                    } else {
                        // if sleep record exists in given day, ask permission to replace it
                        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                        builder.apply {
                            setTitle("Replace Existing Sleep Log")
                            setMessage("This sleep log for this date exists. Are you sure you want to replace it?")
                            setNegativeButton("Cancel") { _, _ ->
                            }
                            setPositiveButton("Replace") { _, _ ->
                                viewModel.updateSleep(duration, quality, date, dateExists)
                                dismiss()
                            }
                            show()
                        }
                    }
                } else {
                    viewModel.updateSleep(duration, quality, date, sleepItem.id)
                    dismiss()
                }
            }
        }
    }


    /**
     * check which chip is checked
     */
    private fun checkedChip(): Int {
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
                return 3
            }
        }
    }
}