package com.example.ivaonesimulation.features.email.new_email

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ivaonesimulation.features.email.new_email.NewEmailComponent.Action

@Composable
fun NewEmailContent(
    component: NewEmailComponent,
) {
    var recipient by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val selectedContactsState by component.selectedContacts.collectAsState() // Подписка на selectedContacts

    // Обновляем recipient при изменении списка выбранных контактов
    LaunchedEffect(selectedContactsState) {
        recipient = selectedContactsState.joinToString(separator = ", ") { it.email }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = recipient,
                onValueChange = { recipient = it },
                label = { Text("Адресат") },
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    component.onAction(Action.SelectContactsClicked)
                }
            ) {
                Icon(Icons.Default.Person, contentDescription = "Выбрать из контактов")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Введите сообщение") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            maxLines = Int.MAX_VALUE
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            enabled = recipient.isNotBlank() && message.isNotBlank()
        ) {
            Text("Отправить")
        }
    }
}
