package com.example.ivaonesimulation.features.chat

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.example.ivaonesimulation.decompose.ScreenComponent

class ChatFeedComponent(
    componentContext: ComponentContext,
    val chatId: String,
    val onForwardClicked: (String, String) -> Unit,
    val forwardMessage: ForwardMessage? = null
) : ScreenComponent(componentContext) {

    @Composable
    override fun Render() {
        val context = LocalContext.current
        Column {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(100) {
                    Row(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        val messageId = "Message $it"
                        Text(
                            text = messageId,
                            modifier = Modifier.weight(1f)
                        )
                        Button(
                            onClick = {
                                onForwardClicked(
                                    chatId,
                                    messageId
                                )
                            }
                        ) {
                            Text("Forward")
                        }
                    }
                }
            }
            forwardMessage?.let {
                Row {
                    Text(
                        text = "Forwarded message: ${it.message} from chat ${it.fromChatId}",
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp)
                    )
                    Button(
                        onClick = {
                            Toast.makeText(
                                context,
                                "Forwarding message: ${it.message} from chat ${it.fromChatId} to chat $chatId",
                                Toast.LENGTH_LONG,
                            ).show()
                        }
                    ) {
                        Text("Send Forward")
                    }
                }
            }
        }
    }

    data class ForwardMessage(
        val message: String,
        val fromChatId: String,
        val callback: () -> Unit,
    )
}