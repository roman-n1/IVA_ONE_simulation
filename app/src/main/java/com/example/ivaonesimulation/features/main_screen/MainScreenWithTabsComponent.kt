package com.example.ivaonesimulation.features.main_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.example.contacts.RootContactsComponent
import com.example.contacts.chooser.ContactsChooserFactoryImpl
import com.example.email.root.RootEmailComponent
import com.example.ivaonesimulation.features.chat.RootChatComponent
import kotlinx.serialization.Serializable
import su.ivcs.one.navigation.BaseComponent

class MainScreenWithTabsComponent(
    componentContext: ComponentContext,
) : BaseComponent,
    ComponentContext by componentContext {
    private val nav = StackNavigation<Child>()

    internal val stack: Value<ChildStack<Child, BaseComponent>> =
        childStack(
            source = nav,
            serializer = Child.serializer(),
            initialConfiguration = Child.ChatFeed,
            childFactory = ::createChild,
        )

    @Serializable
    sealed class Child {

        @Serializable
        data object ChatFeed : Child()

        @Serializable
        data object Contacts : Child()

        @Serializable
        data object Email : Child()
    }

    fun onChatFeedTabClicked() {
        nav.bringToFront(Child.ChatFeed)
    }

    fun onContactsTabClicked() {
        nav.bringToFront(Child.Contacts)
    }

    fun onEmailTabClicked() {
        nav.bringToFront(Child.Email)
    }

    @Composable
    override fun Render() {
        val childStack by stack.subscribeAsState()

        Column {
            Children(
                stack = childStack,
                modifier = Modifier.weight(1F),
            ) {
                it.instance.Render()
            }

            BottomBar(
                component = this@MainScreenWithTabsComponent,
            )
        }
    }

    private fun createChild(
        child: Child,
        componentContext: ComponentContext
    ): BaseComponent =
        when (child) {
            Child.ChatFeed -> {
                RootChatComponent(
                    componentContext = componentContext
                )
            }

            Child.Contacts -> {
                RootContactsComponent(
                    componentContext = componentContext
                )
            }

            Child.Email -> {
                RootEmailComponent(
                    componentContext = componentContext,
                    contactsChooserFactory = ContactsChooserFactoryImpl(),
                )
            }
        }
}