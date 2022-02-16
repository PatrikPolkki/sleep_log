package com.example.sleeplog.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sleeplog.database.Sleep
import com.example.sleeplog.database.SleepDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val sleepdao: SleepDao) : ViewModel() {

    fun getAllSleep(): Flow<List<Sleep>> {
        return sleepdao.getAll()
    }

    suspend fun check(date: Date): Long? {
        return sleepdao.checkIfExists(date)
    }

    suspend fun checkIfExists(date: Date): Flow<Long?> {
        return flow {
            val data = sleepdao.checkIfExists(date)
            emit(data)
        }.flowOn(Dispatchers.IO)
    }

    fun addSleep(date: Date, duration: Long, quality: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            sleepdao.insertSleep(Sleep(0, date, duration, quality))
        }
    }

    fun updateSleepDuration(duration: Long, id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            sleepdao.updateSleepDuration(duration, id)
        }
    }

    fun updateSleepQuality(quality: Int, id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            sleepdao.updateSleepQuality(quality, id)
        }
    }

    fun replaceSleepDuration(duration: Long, date: Date) {
        viewModelScope.launch(Dispatchers.IO) {
            sleepdao.replaceSleepDuration(duration, date)
        }
    }

    fun replaceSleepQuality(quality: Int, date: Date) {
        viewModelScope.launch(Dispatchers.IO) {
            sleepdao.replaceSleepQuality(quality, date)
        }
    }

    fun replaceAll(duration: Long, quality: Int, date: Date) {
        viewModelScope.launch(Dispatchers.IO) {
            sleepdao.replaceAll(duration, quality, date)
        }
    }

    fun deleteSleep(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            sleepdao.deleteSleep(id)
        }
    }
}