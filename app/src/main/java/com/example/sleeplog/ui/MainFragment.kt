package com.example.sleeplog.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sleeplog.R
import com.example.sleeplog.databinding.FragmentMainBinding
import com.example.sleeplog.databinding.SleepDialogBinding
import com.example.sleeplog.utils.DateAndTimeFormatter
import com.example.sleeplog.utils.SwipeToDelete
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(), CellClickListener {

    @Inject
    lateinit var dateFormatter: DateAndTimeFormatter

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)

        binding.sleepRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = SleepAdapter(this@MainFragment)
        }
        setSleepListToRV()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener(fabListener)
        setSwipeHandler()
    }

    private fun setSleepListToRV() {
        val adapter = binding.sleepRv.adapter as SleepAdapter
        lifecycleScope.launch {
            viewModel.getAllSleep().collect {
                if (it.isNotEmpty()) {
                    binding.sleepRv.visibility = View.VISIBLE
                    binding.defaultText.visibility = View.GONE
                    adapter.addSleepList(it)
                } else {
                    binding.sleepRv.visibility = View.GONE
                    binding.defaultText.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setSwipeHandler() {
        val swipeHandler = object : SwipeToDelete(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.sleepRv.adapter as SleepAdapter
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
                        builder.apply {
                            setTitle("Delete Sleep Item")
                            setMessage("Are you sure you want to delete this sleep item?")
                            setNegativeButton("Cancel") { _, _ ->
                                adapter.notifyItemChanged(viewHolder.adapterPosition)
                            }
                            setPositiveButton("Delete") { _, _ ->
                                val id = adapter.deleteSleepItem(viewHolder.adapterPosition)
                                viewModel.deleteSleep(id)
                            }
                            show()
                        }
                    }
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.sleepRv)
    }

    private fun dialogDatePicker(view: TextView) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).setTitleText("Select Date")
            .build()

        activity?.let { datePicker.show(it.supportFragmentManager, datePicker.tag) }

        datePicker.addOnPositiveButtonClickListener {
            view.text = datePicker.headerText
        }
        datePicker.addOnNegativeButtonClickListener {
        }
    }

    private fun addSleepDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val binding = SleepDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)

        binding.date = dateFormatter.getToday()

        binding.hoursPicker.apply {
            minValue = 3
            maxValue = 12
        }
        binding.minutesPicker.apply {
            minValue = 0
            maxValue = 60
        }

        binding.dateButton.setOnClickListener {
            dialogDatePicker(binding.dateText)
        }

        fun returnParameters(): Int? {
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

        builder.apply {
            setPositiveButton("Add") { _, _ ->
                val stringToDate = dateFormatter.stringToDate(binding.dateText.text.toString())
                val checkedChip = returnParameters()
                val duration =
                    dateFormatter.getTime(binding.hoursPicker.value, binding.minutesPicker.value)

                if (checkedChip != null && stringToDate != null) {

                    lifecycleScope.launch {
                        val data = viewModel.check(stringToDate)
                        Log.d("data", data.toString())
                    }

                    viewModel.addSleep(stringToDate, duration, checkedChip)
                }
            }
            setNeutralButton("Cancel") { _, _ ->

            }
            show()
        }
    }

    private val fabListener = View.OnClickListener {
        when (it) {
            binding.floatingActionButton -> {
                addSleepDialog()
            }
        }
    }

    private fun editSleepDialog(duration: Long, quality: Int, date: Date, id: Long) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val binding = SleepDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)

        binding.date = date


        binding.hoursPicker.apply {
            minValue = 0
            maxValue = 24
            value = dateFormatter.getHours(duration)
        }
        binding.minutesPicker.apply {
            minValue = 0
            maxValue = 60
            value = dateFormatter.getMinutes(duration)
        }

        when (quality) {
            1 -> {
                binding.chipGroup.check(R.id.chip_1)
            }
            2 -> {
                binding.chipGroup.check(R.id.chip_2)
            }
            3 -> {
                binding.chipGroup.check(R.id.chip_3)
            }
            4 -> {
                binding.chipGroup.check(R.id.chip_4)
            }
            5 -> {
                binding.chipGroup.check(R.id.chip_5)
            }
        }

        fun returnParameters(): Int? {
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

        builder.apply {
            setPositiveButton("Update") { _, _ ->
                val timeInMillis =
                    dateFormatter.getTime(binding.hoursPicker.value, binding.minutesPicker.value)

                if (timeInMillis != duration && returnParameters() != quality) {
                    viewModel.updateSleepDuration(timeInMillis, id)
                    returnParameters()?.let { viewModel.updateSleepQuality(it, id) }
                }
                if (timeInMillis != duration && returnParameters() == quality) {
                    viewModel.updateSleepDuration(timeInMillis, id)
                }
                if (timeInMillis == duration && returnParameters() != quality) {
                    returnParameters()?.let { viewModel.updateSleepQuality(it, id) }
                }
            }
            setNeutralButton("Cancel") { _, _ ->

            }
            show()
        }
    }

    override fun onCellClickListener(duration: Long, quality: Int, date: Date, id: Long) {
        editSleepDialog(duration, quality, date, id)
    }
}