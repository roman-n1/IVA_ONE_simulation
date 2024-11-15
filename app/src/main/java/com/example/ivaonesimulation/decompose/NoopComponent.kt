package com.example.ivaonesimulation.decompose

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext

class NoopComponent(
    componentContext: ComponentContext
) : ScreenComponent(componentContext) {

    @Composable
    override fun Render() {
        /*do nothing*/
    }
}