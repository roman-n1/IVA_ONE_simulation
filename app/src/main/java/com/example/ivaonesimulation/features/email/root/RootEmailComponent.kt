package com.example.ivaonesimulation.features.email.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.example.ivaonesimulation.features.email.list.EmailListComponent
import kotlinx.serialization.Serializable
import su.ivcs.one.navigation.BaseDecomposeComponent
import su.ivcs.one.navigation.CompositeComponent

class RootEmailComponent(
    componentContext: ComponentContext,
) : CompositeComponent,
    ComponentContext by componentContext {

    private val navigation = StackNavigation<ChildConfiguration>()

    private val stack: Value<ChildStack<ChildConfiguration, BaseDecomposeComponent>> =
        childStack(
            source = navigation,
            serializer = ChildConfiguration.serializer(),
            initialConfiguration = ChildConfiguration.EmailList,
            handleBackButton = true,
            childFactory = ::createChild,
        )

    private fun createChild(
        childConfiguration: ChildConfiguration,
        componentContext: ComponentContext
    ): BaseDecomposeComponent = when (childConfiguration) {
        ChildConfiguration.EmailList -> EmailListComponent(componentContext)
    }

    @Serializable
    sealed interface ChildConfiguration {
        @Serializable
        object EmailList : ChildConfiguration

    }

    override fun getChildStack(): Value<ChildStack<*, BaseDecomposeComponent>> = stack


}
