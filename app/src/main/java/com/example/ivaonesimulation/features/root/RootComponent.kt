package com.example.ivaonesimulation.features.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.example.ivaonesimulation.features.authorization.AuthorizationComponent
import com.example.ivaonesimulation.features.main_screen.MainScreenWithTabsComponent
import kotlinx.serialization.Serializable
import su.ivcs.one.navigation.CompositeComponent

class RootComponent(
    componentContext: ComponentContext,
) : CompositeComponent,
    ComponentContext by componentContext {

    private val navigation = StackNavigation<Child>()

    private val stack: Value<ChildStack<Child, su.ivcs.one.navigation.BaseDecomposeComponent>> =
        childStack(
            source = navigation,
            serializer = Child.serializer(),
            initialStack = {
                listOf<Child>(
                    Child.Authorization
                )
            },
            handleBackButton = true,
            childFactory = ::child,
        )

    override fun getChildStack(): Value<ChildStack<*, su.ivcs.one.navigation.BaseDecomposeComponent>> = stack

    @Serializable
    sealed class Child {

        @Serializable
        data object Authorization : Child()

        @Serializable
        data object MainScreenWithTabs : Child()
    }

    private fun child(
        child: Child,
        childComponentContext: ComponentContext
    ): su.ivcs.one.navigation.BaseDecomposeComponent =
        when (child) {
            is Child.Authorization -> AuthorizationComponent(
                componentContext = childComponentContext,
                tokenCallback = { token ->
                    navigation.replaceAll(Child.MainScreenWithTabs)
                }
            )

            is Child.MainScreenWithTabs -> MainScreenWithTabsComponent(
                componentContext = childComponentContext,
            )
        }
}