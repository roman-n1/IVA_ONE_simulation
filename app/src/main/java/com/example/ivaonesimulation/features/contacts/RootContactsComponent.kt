package com.example.ivaonesimulation.features.contacts

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.example.ivaonesimulation.ComponentRetainedInstance
import com.example.ivaonesimulation.features.contacts.list.ContactListComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.serialization.Serializable

class RootContactsComponent(
    componentContext: ComponentContext,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : su.ivcs.one.navigation.CompositeComponent,
    ComponentContext by componentContext {

    private val coroutineScopeInstance =
        instanceKeeper.getOrCreate { ComponentRetainedInstance(ioDispatcher) }

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

    private fun subscribeOnContactsListNews() = coroutineScopeInstance {
        contactsListComponentNewsFlow.collect { news ->
            when (news) {
                is ContactListComponent.News.ContactClicked -> {
                    navigation.push(
                        Child.ContactDetails(
                            contactId = news.contact.id,
                        )
                    )
                }
            }
        }
    }

    override fun getChildStack(): Value<ChildStack<*, su.ivcs.one.navigation.BaseComponent>> = stack

    @Serializable
    sealed class Child {
        @Serializable
        data object ContactsList : Child()

        @Serializable
        data class ContactDetails(
            val contactId: Int
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

        is Child.ContactDetails -> TODO()
    }
}
