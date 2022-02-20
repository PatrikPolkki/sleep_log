package com.example.sleeplog.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment.STYLE_NORMAL
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sleeplog.R
import com.example.sleeplog.database.Sleep
import com.example.sleeplog.databinding.FragmentMainBinding
import com.example.sleeplog.ui.dialog.DialogFragment
import com.example.sleeplog.utils.DateAndTimeFormatter
import com.example.sleeplog.utils.SwipeToDelete
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(), CellClickListener {

    @Inject
    lateinit var dateFormatter: DateAndTimeFormatter

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)

        // initialize databinding
        binding.apply {
            mainViewModel = viewModel
            lifecycleOwner = this@MainFragment
        }

        // initialize recyclerview
        binding.sleepRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = SleepAdapter(this@MainFragment)
            val itemDecoration: DividerItemDecoration =
                DividerItemDecoration(context, LinearLayoutManager.VERTICAL).apply {
                    ContextCompat.getDrawable(context, R.drawable.divider)?.let { setDrawable(it) }
                }
            addItemDecoration(itemDecoration)
        }


        setSleepListToRV()
        setSwipeHandler()

        binding.floatingActionButton.setOnClickListener(fabListener)

        return binding.root
    }

    /**
     * Collect list flow coming from [viewModel]
     * submit collected list to recyclerview adapter
     */
    private fun setSleepListToRV() {
        val adapter = binding.sleepRv.adapter as SleepAdapter
        lifecycleScope.launch {
            viewModel.getAllSleep().collect {
                if (it.isNotEmpty()) {
                    binding.sleepRv.visibility = View.VISIBLE
                    binding.defaultText.visibility = View.GONE
                    adapter.submitList(it)
                } else {
                    binding.sleepRv.visibility = View.GONE
                    binding.defaultText.visibility = View.VISIBLE
                }
            }
        }
    }

    /**
     * Swipe to delete
     * attach swipeHandler to recyclerview
     * ask the user for confirmation in a pop-up window
     */
    private fun setSwipeHandler() {
        context?.let {
            val swipeHandler = object : SwipeToDelete(it) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = binding.sleepRv.adapter as SleepAdapter
                    when (direction) {
                        ItemTouchHelper.LEFT -> {
                            val sleepItem = adapter.currentList[viewHolder.adapterPosition]
                            val builder: AlertDialog.Builder = AlertDialog.Builder(it)
                            builder.apply {
                                setTitle("Delete Sleep Record")
                                setMessage(
                                    "Are you sure you want to delete this sleep record on ${
                                        dateFormatter.dateMedium(
                                            sleepItem.createdAt
                                        )
                                    }?"
                                )
                                setNegativeButton("Cancel") { _, _ ->
                                    adapter.notifyItemChanged(viewHolder.adapterPosition)
                                }
                                setPositiveButton("Delete") { _, _ ->
                                    viewModel.deleteSleep(sleepItem.id)
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
    }

    /**
     * floating action button opens custom dialog with style
     * send null to "sleepItem" livedata in [viewModel]
     */
    private val fabListener = View.OnClickListener {
        when (it) {
            binding.floatingActionButton -> {
                viewModel.setSleepItem(null)
                val dialog = DialogFragment().apply {
                    setStyle(STYLE_NORMAL, R.style.Theme_SleepLog_Dialog_FullScreen)
                }
                activity?.supportFragmentManager?.let { it1 -> dialog.show(it1, "SLEEPDIALOG") }
            }
        }
    }


    /**
     * By clicking cell on recyclerview opens custom dialog with style
     * send [Sleep] to "sleepItem" livedata in [viewModel]
     */
    override fun onCellClickListener(sleepItem: Sleep) {
        viewModel.setSleepItem(sleepItem)
        val dialog = DialogFragment().apply {
            setStyle(STYLE_NORMAL, R.style.Theme_SleepLog_Dialog_FullScreen)
        }
        activity?.supportFragmentManager?.let { it1 -> dialog.show(it1, "SLEEPDIALOG") }
    }

    override fun onCellLongClickListener(sleepItem: Sleep) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.apply {
            setTitle("Delete Sleep Record")
            setMessage(
                "Are you sure you want to delete this sleep record on ${
                    dateFormatter.dateMedium(
                        sleepItem.createdAt
                    )
                }?"
            )
            setNegativeButton("Cancel") { _, _ ->
            }
            setPositiveButton("Delete") { _, _ ->
                viewModel.deleteSleep(sleepItem.id)
            }
            show()
        }
    }
}