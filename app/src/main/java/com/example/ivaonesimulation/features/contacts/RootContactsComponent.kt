package com.example.ivaonesimulation.features.contacts

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.example.ivaonesimulation.decompose.BaseDecomposeComponent
import com.example.ivaonesimulation.decompose.CompositeComponent
import com.example.ivaonesimulation.decompose.NoopComponent
import kotlinx.serialization.Serializable

class RootContactsComponent(
    componentContext: ComponentContext,
    val searchContact: ((String) -> Unit)? = null,
) : CompositeComponent,
    ComponentContext by componentContext {

    private val navigation = StackNavigation<Child>()

    private val stack: Value<ChildStack<Child, BaseDecomposeComponent>> =
        childStack(
            source = navigation,
            serializer = Child.serializer(),
            initialConfiguration = Child.ContactsList,
            handleBackButton = true,
            childFactory = ::child,
        )

    override fun getChildStack(): Value<ChildStack<*, BaseDecomposeComponent>> = stack

    @Serializable
    sealed class Child {
        @Serializable
        data object Noop : Child()
        @Serializable
        data object ContactsList : Child()
    }

    private fun child(
        child: Child,
        componentContext: ComponentContext
    ): BaseDecomposeComponent = when (child) {
        is Child.Noop -> NoopComponent()
        is Child.ContactsList -> ContactsListComponent(
            componentContext = componentContext,
            searchContact = searchContact,
        )
    }
}
