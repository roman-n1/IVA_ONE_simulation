package com.example.ivaonesimulation.features.email.new_email

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.example.ivaonesimulation.ComponentRetainedInstance
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import su.ivcs.one.navigation.ScreenComponent

class NewEmailComponent(
    componentContext: ComponentContext,
    private val newsFlowCollector: FlowCollector<News>,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ScreenComponent,
    ComponentContext by componentContext {

    private val coroutineScopeInstance =
        instanceKeeper.getOrCreate { ComponentRetainedInstance(ioDispatcher) }

    fun onAction(action: Action) = coroutineScopeInstance {
        when (action) {
            is Action.SelectContactsClicked -> {
                newsFlowCollector.emit(
                    News.SelectContactsClicked
                )
            }
        }
    }

    @Composable
    override fun Render() {
        NewEmailContent(
            component = this
        )
    }

    sealed interface Action {
        object SelectContactsClicked : Action
    }

    sealed interface News {
        object SelectContactsClicked : News
    }
}