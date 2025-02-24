package com.example.contacts.common.toolbar

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import su.ivcs.one.navigation.BaseComponent

class SelectToolbarComponent(
    componentContext: ComponentContext,
    private val messageFlow: Flow<Message>,
) : ComponentContext by componentContext,
    BaseComponent {

    private val coroutineScopeInstance: CoroutineScope = componentContext.coroutineScope()

    private var selectedItemsCount by mutableStateOf(0)

    init {
        subscribeOnMessageFlow()
    }

    private fun subscribeOnMessageFlow() = coroutineScopeInstance.launch {
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
        CenterAlignedTopAppBar(
            title = { Text(text = "Контактов выбрано: $count", color = Color.White) },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.DarkGray,
                titleContentColor = Color.White
            )
        )
    }

    sealed interface Message {
        class SelectedItemsCountChanged(
            val count: Int
        ) : Message
    }
}