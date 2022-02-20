package com.example.sleeplog.di.binding

import dagger.hilt.DefineComponent
import dagger.hilt.components.SingletonComponent

/**
 * DataBindingComponent for DataBinding
 * DataBinding requires a DataBindingComponent if not using static BindingAdapter functions.
 */
@BindingScope
@DefineComponent(parent = SingletonComponent::class)
interface BindingComponent