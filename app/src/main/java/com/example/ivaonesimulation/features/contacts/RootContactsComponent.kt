package com.example.ivaonesimulation.features.contacts

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable

class RootContactsComponent(
    componentContext: ComponentContext,
    val searchContact: ((String) -> Unit)? = null,
) : su.ivcs.one.navigation.CompositeComponent,
    ComponentContext by componentContext {

    private val navigation = StackNavigation<Child>()

    private val stack: Value<ChildStack<Child, su.ivcs.one.navigation.BaseDecomposeComponent>> =
        childStack(
            source = navigation,
            key = RootContactsComponent::class.simpleName.orEmpty(),
            serializer = Child.serializer(),
            initialConfiguration = Child.ContactsList,
            handleBackButton = true,
            childFactory = ::createChild,
        )

    override fun getChildStack(): Value<ChildStack<*, su.ivcs.one.navigation.BaseDecomposeComponent>> = stack

    @Serializable
    sealed class Child {
        @Serializable
        data object Noop : Child()
        @Serializable
        data object ContactsList : Child()
    }

    private fun createChild(
        child: Child,
        componentContext: ComponentContext
    ): su.ivcs.one.navigation.BaseDecomposeComponent = when (child) {
        is Child.Noop -> su.ivcs.one.navigation.NoopComponent()
        is Child.ContactsList -> ContactsListComponent(
            componentContext = componentContext,
            searchContact = searchContact,
        )
    }
}
