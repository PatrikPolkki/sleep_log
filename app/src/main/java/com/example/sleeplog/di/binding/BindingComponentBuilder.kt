package com.example.sleeplog.di.binding

import dagger.hilt.DefineComponent

/**
 * Component builder for DataBindingComponent
 */
@DefineComponent.Builder
interface BindingComponentBuilder {
    fun build(): BindingComponent
}