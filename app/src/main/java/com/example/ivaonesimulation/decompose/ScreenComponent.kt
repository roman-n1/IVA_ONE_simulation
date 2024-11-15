package com.example.ivaonesimulation.decompose

import com.arkivanov.decompose.ComponentContext

abstract class ScreenComponent(
    protected val componentContext: ComponentContext,
) : BaseDecomposeComponent,
    ComponentContext by componentContext {
}
