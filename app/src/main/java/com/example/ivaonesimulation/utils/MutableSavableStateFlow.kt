package com.example.ivaonesimulation.utils

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MutableSavableStateFlow<T>(value: T) : InstanceKeeper.Instance {
    val stateFlow = MutableStateFlow(value)
}

class RebroadcastSateFlow<T>(flow: StateFlow<T>): InstanceKeeper.Instance {
    val stateFlow = flow
}

fun <T>InstanceKeeper.getOrCreateFlow(label: String, value: T): MutableStateFlow<T> = getOrCreate(label) {
    MutableSavableStateFlow(value)
}.stateFlow

fun <T>InstanceKeeper.getOrCreateFlow(label: String, flow: StateFlow<T>): StateFlow<T> = getOrCreate(label) {
    RebroadcastSateFlow(flow)
}.stateFlow