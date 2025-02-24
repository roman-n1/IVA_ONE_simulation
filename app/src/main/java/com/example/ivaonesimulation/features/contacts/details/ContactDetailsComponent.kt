package com.example.ivaonesimulation.features.contacts.details

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.example.models.Contact
import su.ivcs.one.navigation.BaseComponent

class ContactDetailsComponent(
    componentContext: ComponentContext,
    val contact: Contact,
) : ComponentContext by componentContext,
    BaseComponent {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Render() {
        ContactDetailsContent(
            component = this,
        )
    }
}