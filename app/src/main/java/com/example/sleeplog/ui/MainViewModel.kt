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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val sleepdao: SleepDao) : ViewModel() {

    private val mSleepList: MutableLiveData<List<Sleep>> by lazy {
        MutableLiveData<List<Sleep>>()
    }
    val sleepList: LiveData<List<Sleep>>
        get() = mSleepList

    fun getAllSleep(): Flow<List<Sleep>> {
        return sleepdao.getAll()
    }

    /*
        init {
            viewModelScope.launch(Dispatchers.IO) {
                sleepdao.getAll().collect {
                    mSleepList.postValue(it)
                }
            }
        }
    */
    fun addSleep(duration: Float, quality: String) {
        viewModelScope.launch(Dispatchers.Default) {
            sleepdao.insertSleep(Sleep(0, duration, quality))
        }
    }

    fun deleteSleep(id: Long) {
        viewModelScope.launch(Dispatchers.Default) {
            sleepdao.deleteSleep(id)
        }
    }
}