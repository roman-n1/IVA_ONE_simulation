package com.example.ivaonesimulation.features.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.example.ivaonesimulation.decompose.ScreenComponent

class ProfileComponent(
    componentContext: ComponentContext,
) : ScreenComponent,
    ComponentContext by componentContext {

    @Composable
    override fun Render() {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Profile")
        }
    }
}
