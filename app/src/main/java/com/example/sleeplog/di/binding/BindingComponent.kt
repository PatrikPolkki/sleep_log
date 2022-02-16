package com.example.sleeplog.di.binding

import dagger.hilt.DefineComponent
import dagger.hilt.components.SingletonComponent

@BindingScope
@DefineComponent(parent = SingletonComponent::class)
interface BindingComponent