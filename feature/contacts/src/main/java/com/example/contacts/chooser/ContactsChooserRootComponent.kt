package com.example.contacts.chooser

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.example.contacts.details.ContactDetailsComponent
import com.example.contacts_api.chooser.IContactsChooserRootComponent
import com.example.contacts_api.models.Contact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import su.ivcs.one.navigation.BaseComponent
import su.ivcs.one.navigation.CompositeComponent

class ContactsChooserRootComponent(
    componentContext: ComponentContext,
    private val newsFlowCollector: FlowCollector<IContactsChooserRootComponent.News>,
) : IContactsChooserRootComponent,
    CompositeComponent,
    ComponentContext by componentContext {

    private val coroutineScopeInstance: CoroutineScope = componentContext.coroutineScope()

    private val contactsChooserListComponentNewsFlow =
        MutableSharedFlow<ContactsChooserListComponent.News>()

    private val navigation = StackNavigation<ChildConfiguration>()

    private val stack: Value<ChildStack<ChildConfiguration, BaseComponent>> =
        childStack(
            source = navigation,
            key = ContactsChooserRootComponent::class.simpleName.orEmpty(),
            serializer = ChildConfiguration.serializer(),
            initialConfiguration = ChildConfiguration.ContactsList,
            handleBackButton = true,
            childFactory = ::createChild,
        )

    override fun getChildStack(): Value<ChildStack<*, BaseComponent>> = stack

    init {
        subscribeOnContactsChooserListComponentNews()
    }

    private fun subscribeOnContactsChooserListComponentNews() = coroutineScopeInstance.launch {
        contactsChooserListComponentNewsFlow.collect { news ->
            when (news) {
                is ContactsChooserListComponent.News.ContactsSelected -> {
                    newsFlowCollector.emit(
                        IContactsChooserRootComponent.News.ContactsSelected(
                            contacts = news.contacts
                        )
                    )
                }

                is ContactsChooserListComponent.News.ContactClicked -> {
                    navigation.push(
                        ChildConfiguration.ContactDetails(
                            contact = news.contact
                        )
                    )
                }
            }
        }
    }

    private fun createChild(
        childConfiguration: ChildConfiguration,
        componentContext: ComponentContext
    ): BaseComponent = when (childConfiguration) {
        is ChildConfiguration.ContactsList -> {
            ContactsChooserListComponent(
                componentContext = componentContext,
                newsFlowCollector = contactsChooserListComponentNewsFlow
            )
        }

        is ChildConfiguration.ContactDetails -> {
            ContactDetailsComponent(
                componentContext = componentContext,
                contact = childConfiguration.contact,
            )
        }
    }

    @Serializable
    sealed interface ChildConfiguration {
        @Serializable
        object ContactsList : ChildConfiguration

        @Serializable
        data class ContactDetails(
            val contact: Contact,
        ) : ChildConfiguration
    }


}
