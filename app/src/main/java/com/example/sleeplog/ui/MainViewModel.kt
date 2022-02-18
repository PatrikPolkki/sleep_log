package com.example.sleeplog.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sleeplog.database.Sleep
import com.example.sleeplog.database.SleepDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val sleepdao: SleepDao) : ViewModel() {


    init {
        viewModelScope.launch {
            sleepdao.getAverageSleep().collect {
                mSleepAvg.value = it
            }
        }
        viewModelScope.launch {
            sleepdao.getLatestSleep().collect {
                Log.d("FLOW", it.toString())
                mSleepLatest.value = it
                Log.d("SLEEPAVG", sleepAvg.value.toString())
            }
        }

    }

    private val mSleepLatest: MutableLiveData<Long?> by lazy {
        MutableLiveData<Long?>()
    }
    val sleepLatest: LiveData<Long?>
        get() = mSleepLatest

    private val mSleepItem: MutableLiveData<Sleep?> by lazy {
        MutableLiveData<Sleep?>()
    }
    val sleepItem: LiveData<Sleep?>
        get() = mSleepItem

    fun setSleepItem(sleep: Sleep?) {
        mSleepItem.postValue(sleep)
    }

    private val mSleepAvg: MutableLiveData<Long?> by lazy {
        MutableLiveData<Long?>()
    }
    val sleepAvg: LiveData<Long?>
        get() = mSleepAvg


    fun getAllSleep(): Flow<List<Sleep>> {
        return sleepdao.getAll()
    }

    fun deleteSleep(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            sleepdao.deleteSleep(id)
        }
    }

    suspend fun checkDate(date: Date): Long? =
        withContext(Dispatchers.IO) {
            sleepdao.checkIfExists(date)
        }

    fun addSleep(date: Date, duration: Long, quality: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            sleepdao.insertSleep(Sleep(0, date, duration, quality))
        }
    }

    fun updateSleep(duration: Long, quality: Int, date: Date, id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            sleepdao.updateSleep(duration, quality, date, id)
        }
    }
}