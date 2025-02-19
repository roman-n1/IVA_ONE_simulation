package com.example.ivaonesimulation

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ComponentRetainedInstance(
    mainContext: CoroutineContext = Dispatchers.Main.immediate,
    private val cancelCoroutineOnDestroyComponent: Boolean = true
) : InstanceKeeper.Instance {
    val scope = CoroutineScope(mainContext + SupervisorJob())

    operator fun invoke(coroutine: suspend CoroutineScope.() -> Unit) {
        scope.launch {
            coroutine(this)
        }
    }

    override fun onDestroy() {
        if (cancelCoroutineOnDestroyComponent) scope.cancel()
    }
}