package com.example.ivaonesimulation.features.email

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import su.ivcs.one.navigation.ScreenComponent

class RootEmailComponent(
    componentContext: ComponentContext,
) : ScreenComponent,
    ComponentContext by componentContext {

    @Composable
    override fun Render() {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Email")
        }
    }
}
