package com.example.contacts.common.toolbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import su.ivcs.one.navigation.BaseComponent

class DefaultToolbarComponent(
    componentContext: ComponentContext,
    val title: String
) : ComponentContext by componentContext,
    BaseComponent {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Render() {
        TopAppBar(title = { Text(title) })
    }
}