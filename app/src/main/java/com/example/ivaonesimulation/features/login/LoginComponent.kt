package com.example.ivaonesimulation.features.login

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.example.ivaonesimulation.decompose.ScreenComponent

class LoginComponent(
    componentContext: ComponentContext,
    private val tokenCallback: (String) -> Unit,
    private val registerClicked: () -> Unit,
) : ScreenComponent(componentContext) {

    @Composable
    override fun Render() {
        LoginContent(
            tokenCallback = tokenCallback,
            registerClicked = registerClicked
        )
    }
}



