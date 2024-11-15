package com.example.ivaonesimulation.features.register

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.example.ivaonesimulation.decompose.ScreenComponent

class RegisterComponent(
    componentContext: ComponentContext,
    private val registerFinished: (String) -> Unit
) :
    ScreenComponent,
    ComponentContext by componentContext {

    @Composable
    override fun Render() {
        RegisterContent(
            registerFinished = registerFinished
        )
    }
}
