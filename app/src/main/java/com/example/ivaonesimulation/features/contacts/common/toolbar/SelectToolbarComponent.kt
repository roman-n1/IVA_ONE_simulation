package com.example.ivaonesimulation.features.contacts.common.toolbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.example.ivaonesimulation.ComponentRetainedInstance
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import su.ivcs.one.navigation.BaseComponent

class SelectToolbarComponent(
    componentContext: ComponentContext,
    private val messageFlow: Flow<Message>,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ComponentContext by componentContext,
    BaseComponent {

    private val coroutineScopeInstance =
        instanceKeeper.getOrCreate { ComponentRetainedInstance(ioDispatcher) }

    private var selectedItemsCount by mutableStateOf(0)

    init {
        subscribeOnMessageFlow()
    }

    private fun subscribeOnMessageFlow() = coroutineScopeInstance {
        messageFlow.collect { message ->
            when (message) {
                is Message.SelectedItemsCountChanged -> {
                    selectedItemsCount = message.count
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Render() {
        val count by remember { derivedStateOf { selectedItemsCount } }
        TopAppBar(
            title = { Text("Контактов выбрано: $count") }
        )
    }

    sealed interface Message {
        class SelectedItemsCountChanged(
            val count: Int
        ) : Message
    }
}