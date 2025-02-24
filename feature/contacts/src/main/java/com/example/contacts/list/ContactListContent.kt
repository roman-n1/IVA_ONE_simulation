package com.example.contacts.list

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun ContactListContent(
    contactListComponent: ContactListComponent,
) {
    Column {
        val toolbarChild = contactListComponent.toolbarSlot.subscribeAsState()
        toolbarChild.value.child?.instance?.let { it.Render() }
        contactListComponent.listPartComponent.Render()
    }
}