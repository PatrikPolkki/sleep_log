package com.example.sleeplog

import android.app.Application
import androidx.databinding.DataBindingUtil
import com.example.sleeplog.di.binding.BindingComponentBuilder
import com.example.sleeplog.di.binding.DataBindingEntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import javax.inject.Provider

/**
 * Application class for Hilt
 * Initialize usage of binding adapters
 */
@HiltAndroidApp
class BaseApplication : Application() {
    @Inject
    lateinit var bindingComponentProvider: Provider<BindingComponentBuilder>

    override fun onCreate() {
        super.onCreate()
        val dataBindingComponent = bindingComponentProvider.get().build()
        val dataBindingEntryPoint =
            EntryPoints.get(dataBindingComponent, DataBindingEntryPoint::class.java)
        DataBindingUtil.setDefaultComponent(dataBindingEntryPoint)

    }
}