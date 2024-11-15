package com.example.ivaonesimulation.decompose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface CompositeComponent : BaseDecomposeComponent {

    fun getChildStack(): Value<ChildStack<*, BaseDecomposeComponent>>

    @Composable
    override fun Render() {
        val childStack by getChildStack().subscribeAsState()

        Children(
            stack = childStack,
        ) {
            it.instance.Render()
        }
    }
}
