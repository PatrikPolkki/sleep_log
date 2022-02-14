package com.example.sleeplog.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sleeplog.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {

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
            adapter = SleepAdapter()
        }
        setSleepListToRV()


        binding.floatingActionButton.setOnClickListener {
            Log.d("TOImi", "TOimi")
            viewModel.addSleep(5F, "GOOD")
        }

        return binding.root
    }

    private fun setSleepListToRV() {
        val adapter = binding.sleepRv.adapter as SleepAdapter
        lifecycleScope.launch {
            viewModel.getAllSleep().collect {
                if (it.isNotEmpty()) {
                    binding.sleepRv.visibility = View.VISIBLE
                    binding.defaultText.visibility = View.GONE
                    adapter.addSleep(it)
                } else {
                    binding.sleepRv.visibility = View.GONE
                    binding.defaultText.visibility = View.VISIBLE
                }
            }
        }
    }
}