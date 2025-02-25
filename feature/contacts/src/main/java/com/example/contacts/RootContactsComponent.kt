package com.example.contacts

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.example.contacts.details.ContactDetailsComponent
import com.example.contacts.list.ContactListComponent
import com.example.contacts_api.models.Contact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import su.ivcs.one.navigation.CompositeComponent

class RootContactsComponent(
    componentContext: ComponentContext,
) : CompositeComponent,
    ComponentContext by componentContext {

    private val coroutineScopeInstance: CoroutineScope = componentContext.coroutineScope()

    private val contactsListComponentNewsFlow =
        MutableSharedFlow<ContactListComponent.News>()

    private val navigation = StackNavigation<Child>()

    private val stack: Value<ChildStack<Child, su.ivcs.one.navigation.BaseComponent>> =
        childStack(
            source = navigation,
            key = RootContactsComponent::class.simpleName.orEmpty(),
            serializer = Child.serializer(),
            initialConfiguration = Child.ContactsList,
            handleBackButton = true,
            childFactory = ::createChild,
        )

    init {
        subscribeOnContactsListNews()
    }

    private fun subscribeOnContactsListNews() = coroutineScopeInstance.launch {
        contactsListComponentNewsFlow.collect { news ->
            when (news) {
                is ContactListComponent.News.ContactClicked -> {
                    navigation.push(
                        Child.ContactDetails(
                            contact = news.contact,
                        )
                    )
                }
            }
        }
    }

    override fun getChildStack(): Value<ChildStack<*, su.ivcs.one.navigation.BaseComponent>> = stack

    @kotlinx.serialization.Serializable
    sealed class Child {
        @kotlinx.serialization.Serializable
        data object ContactsList : Child()

        @Serializable
        data class ContactDetails(
            val contact: Contact
        ) : Child()
    }

    private fun createChild(
        child: Child,
        componentContext: ComponentContext
    ): su.ivcs.one.navigation.BaseComponent = when (child) {

        is Child.ContactsList -> ContactListComponent(
            componentContext = componentContext,
            newsFlowCollector = contactsListComponentNewsFlow,
        )

        is Child.ContactDetails -> ContactDetailsComponent(
            componentContext = componentContext,
            contact = child.contact,
        )
    }
}
