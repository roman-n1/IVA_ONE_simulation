package com.example.email.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.example.contacts_api.chooser.ContactsChooserFactory
import com.example.email.list.EmailListComponent
import com.example.email.root_new_email.RootNewEmailComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import su.ivcs.one.navigation.BaseComponent
import su.ivcs.one.navigation.CompositeComponent

class RootEmailComponent(
    componentContext: ComponentContext,
    private val contactsChooserFactory: ContactsChooserFactory,
) : CompositeComponent,
    ComponentContext by componentContext {

    private val coroutineScopeInstance: CoroutineScope = componentContext.coroutineScope()

    private val emailListComponentNewsFlow = MutableSharedFlow<EmailListComponent.News>()

    private val navigation = StackNavigation<ChildConfiguration>()

    private val stack: Value<ChildStack<ChildConfiguration, BaseComponent>> =
        childStack(
            source = navigation,
            key = RootEmailComponent::class.simpleName.orEmpty(),
            serializer = ChildConfiguration.serializer(),
            initialConfiguration = ChildConfiguration.EmailList,
            handleBackButton = true,
            childFactory = ::createChild,
        )

    init {
        subscribeOnEmailListComponentNews()
    }

    @OptIn(DelicateDecomposeApi::class)
    private fun subscribeOnEmailListComponentNews() = coroutineScopeInstance.launch {
        emailListComponentNewsFlow.collect { news ->
            when (news) {
                is EmailListComponent.News.NewEmailButtonClicked -> {
                    navigation.push(ChildConfiguration.NewEmail)
                }
            }
        }
    }

    private fun createChild(
        childConfiguration: ChildConfiguration,
        componentContext: ComponentContext
    ): BaseComponent = when (childConfiguration) {
        is ChildConfiguration.EmailList -> {
            EmailListComponent(
                componentContext = componentContext,
                newsFlowCollector = emailListComponentNewsFlow,
            )
        }

        is ChildConfiguration.NewEmail -> {
            RootNewEmailComponent(
                componentContext = componentContext,
                contactsChooserFactory = contactsChooserFactory,
            )
        }
    }

    @Serializable
    sealed interface ChildConfiguration {
        @Serializable
        object EmailList : ChildConfiguration

        @Serializable
        object NewEmail : ChildConfiguration
    }

    override fun getChildStack(): Value<ChildStack<*, BaseComponent>> = stack


}
