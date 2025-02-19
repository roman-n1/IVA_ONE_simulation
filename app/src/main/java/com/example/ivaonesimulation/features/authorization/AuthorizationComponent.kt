package com.example.ivaonesimulation.features.authorization

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.example.ivaonesimulation.features.login.LoginComponent
import com.example.ivaonesimulation.features.register.RegisterComponent
import kotlinx.serialization.Serializable
import su.ivcs.one.navigation.CompositeComponent
import su.ivcs.one.navigation.NoopComponent

class AuthorizationComponent(
    componentContext: ComponentContext,
    private val tokenCallback: (String) -> Unit,
    getTokenUseCase: GetTokenUseCase = GetTokenUseCase(haveToken = false),
) : CompositeComponent,
    ComponentContext by componentContext {

    @Serializable
    sealed class Child {
        @Serializable
        data object Noop : Child()

        @Serializable
        data object Login : Child()

        @Serializable
        data object Register : Child()
    }

    private val navigation = StackNavigation<Child>()

    private val stack: Value<ChildStack<Child, su.ivcs.one.navigation.BaseDecomposeComponent>> =
        childStack(
            source = navigation,
            serializer = Child.serializer(),
            initialConfiguration = Child.Noop,
            handleBackButton = true,
            childFactory = ::child,
        )

    init {
        val token = getTokenUseCase()
        if (token != null) {
            tokenCallback(token)
        } else {
            navigation.replaceAll(Child.Login)
        }
    }

    @OptIn(DelicateDecomposeApi::class)
    private fun child(
        child: Child,
        componentContext: ComponentContext
    ): su.ivcs.one.navigation.BaseDecomposeComponent = when (child) {

        is Child.Noop -> NoopComponent()

        is Child.Login -> LoginComponent(
            componentContext = componentContext,
            tokenCallback = tokenCallback,
            registerClicked = { navigation.push(Child.Register) }
        )

        is Child.Register -> RegisterComponent(
            componentContext = componentContext,
            registerFinished = { token ->
                tokenCallback(token)
            }
        )
    }

    override fun getChildStack(): Value<ChildStack<*, su.ivcs.one.navigation.BaseDecomposeComponent>> =
        stack
}