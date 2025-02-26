package com.example.ivaonesimulation.features.chat

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.example.contacts.RootContactsComponent
import kotlinx.serialization.Serializable
import su.ivcs.one.navigation.CompositeComponent

abstract class AbstractChatComponent(
    componentContext: ComponentContext,
) : CompositeComponent, ComponentContext by componentContext {

    @Serializable
    sealed class Child {
        @Serializable
        data class Feed(
            val chatId: String,
        ) : Child()

        @Serializable
        data class ForwardMessage(
            val message: String,
            val fromChatId: String,
            val toPersonId: String,
        ) : Child()

        @Serializable
        data object ChatList : Child()

        @Serializable
        data object SelectContact : Child()
    }
}

class RootChatComponent(
    componentContext: ComponentContext,
) : AbstractChatComponent(componentContext) {

    private val navigation = StackNavigation<Child>()

    private val stack: Value<ChildStack<Child, su.ivcs.one.navigation.BaseComponent>> =
        childStack(
            source = navigation,
            key = RootChatComponent::class.simpleName.orEmpty(),
            serializer = Child.serializer(),
            initialConfiguration = Child.ChatList,
            handleBackButton = true,
            childFactory = ::createChild,
        )

    override fun getChildStack(): Value<ChildStack<*, su.ivcs.one.navigation.BaseComponent>> = stack

    private var messageToForward: String? = null

    private var messageFromChatId: String? = null

    @OptIn(DelicateDecomposeApi::class)
    private fun createChild(
        child: Child,
        componentContext: ComponentContext
    ): su.ivcs.one.navigation.BaseComponent = when (child) {
        is Child.ChatList -> ChatListComponent(
            componentContext = componentContext,
            onChatClicked = { id ->
                navigation.push(Child.Feed(id))
            },
        )

        is Child.Feed -> ChatFeedComponent(
            componentContext = componentContext,
            chatId = child.chatId,
            onForwardClicked = { chatId, message ->
                messageToForward = message
                messageFromChatId = chatId
                navigation.push(Child.SelectContact)
            },
        )

        is Child.SelectContact -> com.example.contacts.RootContactsComponent(
            componentContext = componentContext,
        )

        is Child.ForwardMessage -> ChatFeedComponent(
            componentContext = componentContext,
            chatId = child.toPersonId,
            onForwardClicked = { chatId, message ->
                messageToForward = message
                messageFromChatId = chatId
                navigation.push(Child.SelectContact)
            },
            forwardMessage = ChatFeedComponent.ForwardMessage(
                message = child.message,
                fromChatId = child.fromChatId,
                callback = {
                    messageToForward = null
                    messageFromChatId = null
                },
            ),
        )
    }
}
