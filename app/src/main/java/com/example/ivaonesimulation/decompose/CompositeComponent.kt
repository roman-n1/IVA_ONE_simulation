package com.example.ivaonesimulation.decompose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.value.Value

abstract class CompositeComponent<C : Any>(
    protected val componentContext: ComponentContext,
) : BaseDecomposeComponent, ComponentContext by componentContext {
    protected val navigation = StackNavigation<C>()

    protected abstract val stack: Value<ChildStack<C, BaseDecomposeComponent>>

    @Composable
    override fun Render() {
        val childStack by stack.subscribeAsState()

        Children(
            stack = childStack,
        ) {
            it.instance.Render()
        }
    }
}
