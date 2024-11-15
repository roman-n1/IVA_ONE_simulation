package com.example.ivaonesimulation.features.chat

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.example.ivaonesimulation.decompose.BaseDecomposeComponent
import com.example.ivaonesimulation.decompose.CompositeComponent
import com.example.ivaonesimulation.features.contacts.RootContactsComponent
import kotlinx.serialization.Serializable

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

    private val stack: Value<ChildStack<Child, BaseDecomposeComponent>> =
        childStack(
            source = navigation,
            serializer = Child.serializer(),
            initialConfiguration = Child.ChatList,
            handleBackButton = true,
            childFactory = ::child,
        )

    override fun getChildStack(): Value<ChildStack<*, BaseDecomposeComponent>> = stack

    private var messageToForward: String? = null

    private var messageFromChatId: String? = null

    @OptIn(DelicateDecomposeApi::class)
    private fun child(
        child: Child,
        componentContext: ComponentContext
    ): BaseDecomposeComponent = when (child) {
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

        is Child.SelectContact -> RootContactsComponent(
            componentContext = componentContext,
            searchContact = { personId ->
                navigation.push(
                    Child.ForwardMessage(
                        message = messageToForward.orEmpty(),
                        fromChatId = messageFromChatId.orEmpty(),
                        toPersonId = personId,
                    )
                )
            }
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
