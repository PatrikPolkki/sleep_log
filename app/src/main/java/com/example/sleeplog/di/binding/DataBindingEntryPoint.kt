package com.example.sleeplog.di.binding

import androidx.databinding.DataBindingComponent
import com.example.sleeplog.utils.BindingAdapters
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn

/**
 * EntryPoint for DataBindingComponent
 */

@EntryPoint
@BindingScope
@InstallIn(BindingComponent::class)
interface DataBindingEntryPoint : DataBindingComponent {

    @BindingScope
    override fun getBindingAdapters(): BindingAdapters
}