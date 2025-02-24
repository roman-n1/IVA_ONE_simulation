package com.example.ivaonesimulation.features.contacts.chooser

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.example.ivaonesimulation.ComponentRetainedInstance
import com.example.ivaonesimulation.common_models.Contact
import com.example.ivaonesimulation.features.contacts.details.ContactDetailsComponent
import com.example.ivaonesimulation.features.email.root.RootEmailComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.serialization.Serializable
import su.ivcs.one.navigation.BaseComponent
import su.ivcs.one.navigation.CompositeComponent

class ContactsChooserRootComponent(
    componentContext: ComponentContext,
    override val newsFlowCollector: FlowCollector<IContactsChooserRootComponent.News>,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : IContactsChooserRootComponent,
    CompositeComponent,
    ComponentContext by componentContext {

    private val coroutineScopeInstance =
        instanceKeeper.getOrCreate { ComponentRetainedInstance(ioDispatcher) }

    private val contactsChooserListComponentNewsFlow =
        MutableSharedFlow<ContactsChooserListComponent.News>()

    private val navigation = StackNavigation<ChildConfiguration>()

    private val stack: Value<ChildStack<ChildConfiguration, BaseComponent>> =
        childStack(
            source = navigation,
            key = RootEmailComponent::class.simpleName.orEmpty(),
            serializer = ChildConfiguration.serializer(),
            initialConfiguration = ChildConfiguration.ContactsList,
            handleBackButton = true,
            childFactory = ::createChild,
        )

    override fun getChildStack(): Value<ChildStack<*, BaseComponent>> = stack

    init {
        subscribeOnContactsChooserListComponentNews()
    }

    private fun subscribeOnContactsChooserListComponentNews() = coroutineScopeInstance {
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
