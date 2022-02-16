package com.example.sleeplog.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sleeplog.database.Sleep
import com.example.sleeplog.databinding.SleepItemBinding

class SleepAdapter(private val cellClickListener: CellClickListener) :
    RecyclerView.Adapter<SleepAdapter.ViewHolder>() {

    private val sleepList: MutableList<Sleep> = mutableListOf()
    private var cellIndex: Int? = null

    fun addSleepList(list: List<Sleep>) {
        sleepList.let {
            if (list.size != it.size) {
                sleepList.clear()
                sleepList.addAll(list)
                notifyItemRangeInserted(it.size, list.size)
            } else {
                cellIndex?.let { index ->
                    sleepList.clear()
                    sleepList.addAll(list)
                    notifyItemChanged(index)
                }
            }
        }
    }

    fun deleteSleepItem(pos: Int): Long {
        val item = sleepList[pos]
        sleepList.removeAt(pos)
        notifyItemRemoved(pos)
        return item.id
    }


    inner class ViewHolder(val binding: SleepItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SleepItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = sleepList[position]

        holder.binding.sleepItem = item
        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(
                item.sleepDuration,
                item.sleepQuality,
                item.createdAt,
                item.id
            )
            cellIndex = holder.adapterPosition
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = sleepList.size
}