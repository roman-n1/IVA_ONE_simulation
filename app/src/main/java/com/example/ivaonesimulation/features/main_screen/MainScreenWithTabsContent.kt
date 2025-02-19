package com.example.ivaonesimulation.features.main_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.ivaonesimulation.features.chat.RootChatComponent
import com.example.ivaonesimulation.features.contacts.RootContactsComponent
import com.example.ivaonesimulation.features.email.root.RootEmailComponent

@Composable
fun BottomBar(component: MainScreenWithTabsComponent, modifier: Modifier = Modifier) {
    val stack by component.stack.subscribeAsState()
    val activeComponent = stack.active.instance

    NavigationBar {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NavigationBarItem(
                selected = activeComponent is RootChatComponent,
                label = {
                    Text("Chat Feed")
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Chat Feed"
                    )
                },
                onClick = {
                    component.onChatFeedTabClicked()
                }
            )
            NavigationBarItem(
                selected = activeComponent is RootContactsComponent,
                label = {
                    Text("Contacts")
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Contacts"
                    )
                },
                onClick = {
                    component.onContactsTabClicked()
                }
            )
            NavigationBarItem(
                selected = activeComponent is RootEmailComponent,
                label = {
                    Text("Email")
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email"
                    )
                },
                onClick = {
                    component.onEmailTabClicked()
                }
            )
        }
    }
}