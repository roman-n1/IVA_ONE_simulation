package com.example.ivaonesimulation.features.email.list

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import su.ivcs.one.navigation.ScreenComponent

class EmailListComponent(
    componentContext: ComponentContext
) : ScreenComponent,
    ComponentContext by componentContext {

    @Composable
    override fun Render() {
        EmailListContent(
            component = this
        )
    }
}