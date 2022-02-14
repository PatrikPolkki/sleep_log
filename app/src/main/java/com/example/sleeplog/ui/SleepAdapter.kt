package com.example.sleeplog.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sleeplog.database.Sleep
import com.example.sleeplog.databinding.SleepItemBinding

class SleepAdapter() : RecyclerView.Adapter<SleepAdapter.ViewHolder>() {

    private val sleepList: MutableList<Sleep> = mutableListOf()

    fun addSleep(list: List<Sleep>) {
        sleepList.clear()
        sleepList.let {
            sleepList.addAll(list)
            notifyItemRangeInserted(it.size, list.size)
        }
    }

    fun deleteSleep(pos: Int): Long {
        val item = sleepList[pos]
        sleepList.removeAt(pos)
        notifyItemRemoved(pos)
        return item.id
    }


    inner class ViewHolder(private val binding: SleepItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Sleep) {
            binding.sleepItem = item

            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SleepItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = sleepList[position]
        holder.bind(item)

    }

    override fun getItemCount(): Int = sleepList.size
}