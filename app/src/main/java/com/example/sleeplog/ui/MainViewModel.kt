package com.example.sleeplog.ui

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

    // mutable livedata for Sleep item and getter for it
    private val mSleepItem: MutableLiveData<Sleep?> by lazy {
        MutableLiveData<Sleep?>()
    }
    val sleepItem: LiveData<Sleep?>
        get() = mSleepItem

    // mutable livedata for average sleep duration and getter for it
    private val mSleepAvg: MutableLiveData<Long?> by lazy {
        MutableLiveData<Long?>()
    }
    val sleepAvg: LiveData<Long?>
        get() = mSleepAvg

    // mutable livedata for latest sleep duration and getter for it
    private val mSleepLatest: MutableLiveData<Long?> by lazy {
        MutableLiveData<Long?>()
    }
    val sleepLatest: LiveData<Long?>
        get() = mSleepLatest

    /**
     * get sleep average and and latest sleep  from database
     * dao returns flow of Long
     */
    init {
        viewModelScope.launch {
            sleepdao.getAverageSleep().collect {
                mSleepAvg.value = it
            }
        }
        viewModelScope.launch {
            sleepdao.getLatestSleep().collect {
                mSleepLatest.value = it
            }
        }

    }

    // post Sleep to mSleepItem
    fun setSleepItem(sleep: Sleep?) {
        mSleepItem.postValue(sleep)
    }

    // return Flow of Sleep list
    fun getAllSleep(): Flow<List<Sleep>> {
        return sleepdao.getAll()
    }

    // delete Sleep item on given id
    fun deleteSleep(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            sleepdao.deleteSleep(id)
        }
    }

    // returns id of sleep item if it exists on given date
    suspend fun checkDate(date: Date): Long? =
        withContext(Dispatchers.IO) {
            sleepdao.checkIfExists(date)
        }

    // insert new sleep item with given values
    fun addSleep(date: Date, duration: Long, quality: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            sleepdao.insertSleep(Sleep(0, date, duration, quality))
        }
    }

    // update existing sleep item with given values
    fun updateSleep(duration: Long, quality: Int, date: Date, id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            sleepdao.updateSleep(duration, quality, date, id)
        }
    }
}