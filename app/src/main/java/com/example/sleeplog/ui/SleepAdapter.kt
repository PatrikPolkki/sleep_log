package com.example.sleeplog.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sleeplog.database.Sleep
import com.example.sleeplog.databinding.SleepItemBinding

class SleepAdapter(private val cellClickListener: CellClickListener) :
    ListAdapter<Sleep, SleepAdapter.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Sleep>() {
            override fun areItemsTheSame(oldItem: Sleep, newItem: Sleep): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Sleep, newItem: Sleep): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class ViewHolder(val binding: SleepItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SleepItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.sleepItem = item

        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(item)
        }
    }
}