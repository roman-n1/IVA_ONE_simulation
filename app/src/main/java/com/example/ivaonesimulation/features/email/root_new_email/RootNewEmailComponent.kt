package com.example.ivaonesimulation.features.email.root_new_email

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.example.ivaonesimulation.ComponentRetainedInstance
import com.example.ivaonesimulation.features.contacts.chooser.ContactsChooserRootComponent
import com.example.ivaonesimulation.features.contacts.chooser.IContactsChooserRootComponent
import com.example.ivaonesimulation.features.email.new_email.NewEmailComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.serialization.Serializable
import su.ivcs.one.navigation.BaseComponent
import su.ivcs.one.navigation.CompositeComponent

class RootNewEmailComponent(
    componentContext: ComponentContext,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CompositeComponent,
    ComponentContext by componentContext {

    private val coroutineScopeInstance =
        instanceKeeper.getOrCreate { ComponentRetainedInstance(ioDispatcher) }

    private val newEmailComponentNewsFlow = MutableSharedFlow<NewEmailComponent.News>()
    private val newEmailComponentMessageFlow = MutableSharedFlow<NewEmailComponent.Message>()
    private val contactsChooserRootComponentNewsFlow =
        MutableSharedFlow<IContactsChooserRootComponent.News>()

    private val navigation = StackNavigation<ChildConfiguration>()

    private val stack: Value<ChildStack<ChildConfiguration, BaseComponent>> =
        childStack(
            source = navigation,
            key = RootNewEmailComponent::class.simpleName.orEmpty(),
            serializer = ChildConfiguration.serializer(),
            initialConfiguration = ChildConfiguration.NewEmail,
            handleBackButton = true,
            childFactory = ::createChild,
        )

    init {
        subscribeOnNewEmailComponentNews()
        subscribeOnContactsChooserRootComponentNews()
    }

    private fun subscribeOnContactsChooserRootComponentNews() = coroutineScopeInstance {
        contactsChooserRootComponentNewsFlow.collect { news ->
            when (news) {
                is IContactsChooserRootComponent.News.ContactsSelected -> {
                    navigation.pop {
                        coroutineScopeInstance {
                            newEmailComponentMessageFlow.emit(
                                NewEmailComponent.Message.ContactsSelected(
                                    contacts = news.contacts
                                )
                            )
                        }
                    }
                }

                IContactsChooserRootComponent.News.CancelClicked -> {
                    navigation.pop()
                }
            }
        }
    }

    @OptIn(DelicateDecomposeApi::class)
    private fun subscribeOnNewEmailComponentNews() = coroutineScopeInstance {
        newEmailComponentNewsFlow.collect { news ->
            when (news) {
                is NewEmailComponent.News.SelectContactsClicked -> {
                    navigation.push(ChildConfiguration.SelectContacts)
                }
            }
        }
    }

    private fun createChild(
        childConfiguration: ChildConfiguration,
        componentContext: ComponentContext
    ): BaseComponent =
        when (childConfiguration) {
            ChildConfiguration.NewEmail -> NewEmailComponent(
                componentContext = componentContext,
                newsFlowCollector = newEmailComponentNewsFlow,
                messageFlow = newEmailComponentMessageFlow
            )

            ChildConfiguration.SelectContacts -> ContactsChooserRootComponent(
                componentContext = componentContext,
                newsFlowCollector = contactsChooserRootComponentNewsFlow,
            )
        }

    @OptIn(DelicateDecomposeApi::class)
    fun onAction(action: Action) = coroutineScopeInstance {
        when (action) {
            is Action.SelectContacts -> {
                navigation.push(
                    ChildConfiguration.SelectContacts
                )
            }
        }
    }

    @Serializable
    sealed interface ChildConfiguration {
        @Serializable
        object NewEmail : ChildConfiguration

        @Serializable
        object SelectContacts : ChildConfiguration

    }

    sealed interface Action {
        object SelectContacts : Action
    }

    override fun getChildStack(): Value<ChildStack<*, BaseComponent>> = stack
}