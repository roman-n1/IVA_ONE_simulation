package com.example.ivaonesimulation.features.email.list

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.example.ivaonesimulation.ComponentRetainedInstance
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import su.ivcs.one.navigation.ScreenComponent

class EmailListComponent(
    componentContext: ComponentContext,
    private val newsFlowCollector: FlowCollector<News>,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ScreenComponent,
    ComponentContext by componentContext {

    private val coroutineScopeInstance =
        instanceKeeper.getOrCreate { ComponentRetainedInstance(ioDispatcher) }

    fun onAction(action: Action) = coroutineScopeInstance {
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