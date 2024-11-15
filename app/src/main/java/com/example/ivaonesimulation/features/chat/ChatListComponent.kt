package com.example.ivaonesimulation.features.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.example.ivaonesimulation.decompose.ScreenComponent

class ChatListComponent(
    componentContext: ComponentContext,
    val onChatClicked: (String) -> Unit
) : ScreenComponent(componentContext) {

    @Composable
    override fun Render() {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(100) {
                val id = "Chat $it"
                Text(
                    text  = id,
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            onChatClicked(id)
                        }
                )
            }
        }
    }
}