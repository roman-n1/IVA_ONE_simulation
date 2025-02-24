package com.example.contacts.common.contact_list

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.models.Contact

@Composable
fun ContactListPartContent(
    component: ContactsListPartComponent
) {
    val state by component.chatState.collectAsState()

    val contacts: List<Contact> = state.contacts
    val selectedContacts: List<Contact> = state.selectedContacts

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.contacts.size) { item ->
            ContactItem(
                contact = contacts[item],
                isSelected = selectedContacts.contains(contacts[item]),
                onClick = { component.onAction(ContactsListPartComponent.Action.OnContactClicked(contacts[item])) },
                onLongPress = { component.onAction(ContactsListPartComponent.Action.OnContactLongPressed(contacts[item])) }
            )
        }
    }
}

@Composable
fun ContactItem(contact: Contact, isSelected: Boolean, onClick: () -> Unit, onLongPress: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() },
                    onLongPress = { onLongPress() }
                )
            }

            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (isSelected) Color.Green else Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = Color.White
                )
            } else {
                Text(
                    text = "${contact.name.firstOrNull()?.uppercaseChar() ?: ""}${contact.name.split(" ").getOrNull(1)?.firstOrNull()?.uppercaseChar() ?: ""}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = contact.name, fontWeight = FontWeight.Bold)
            Text(text = contact.email, style = MaterialTheme.typography.bodySmall)
            Text(text = contact.phone, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Preview
@Composable
fun ContactItemPreview() {
    ContactItem(
        contact = Contact(
            id = 1,
            name = "John Doe",
            email = "j@j.com",
            phone = "+1234567890"
        ),
        isSelected = false,
        onClick = {},
        onLongPress = {},
    )
}

@Preview
@Composable
fun ContactSelectedItemPreview() {
    ContactItem(
        contact = Contact(
            id = 1,
            name = "John Doe",
            email = "j@j.com",
            phone = "+1234567890"
        ),
        isSelected = true,
        onClick = {},
        onLongPress = {},
    )
}
