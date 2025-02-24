package com.example.email.list

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import su.ivcs.one.navigation.ScreenComponent

class EmailListComponent(
    componentContext: ComponentContext,
    private val newsFlowCollector: FlowCollector<News>,
) : ScreenComponent,
    ComponentContext by componentContext {

    private val coroutineScopeInstance: CoroutineScope = componentContext.coroutineScope()

    fun onAction(action: Action) = coroutineScopeInstance.launch {
        when (action) {
            is Action.NewEmailButtonClicked -> newsFlowCollector.emit(
                News.NewEmailButtonClicked
            )
        }
    }

    @Composable
    override fun Render() {
        EmailListContent(
            component = this
        )
    }

    sealed interface Action {
        object NewEmailButtonClicked : Action
    }

    sealed interface News {
        object NewEmailButtonClicked : News
    }
}