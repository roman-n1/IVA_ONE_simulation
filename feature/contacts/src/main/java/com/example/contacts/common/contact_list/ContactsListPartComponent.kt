package com.example.contacts.common.contact_list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.example.contacts.utils.getOrCreateFlow
import com.example.models.Contact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import su.ivcs.one.navigation.ScreenComponent

class ContactsListPartComponent(
    componentContext: ComponentContext,
    contactsList: List<Contact>,
    private val newsFlowCollector: FlowCollector<News>,
    private val messageFlow: Flow<Message>,
) : ScreenComponent,
    ComponentContext by componentContext {

    private val coroutineScopeInstance: CoroutineScope = componentContext.coroutineScope()


    val chatState = instanceKeeper.getOrCreateFlow(
        label = "ContactsListComponent_State",
        value = ContactsListState(
            contacts = contactsList,
            selectedContacts = emptyList()
        )
    )

    init {
        subscribeOnMessageFlow()
    }

    private fun subscribeOnMessageFlow() = coroutineScopeInstance.launch {
        messageFlow.collect { message ->
            when (message) {
                is Message.ContactListUpdated -> {
                    chatState.update {
                        it.copy(contacts = message.contacts)
                    }
                }

                is Message.SelectContact -> {
                    chatState.update {
                        it.copy(selectedContacts = it.selectedContacts + message.contact)
                    }
                }

                is Message.UnselectContact -> {
                    chatState.update {
                        it.copy(selectedContacts = it.selectedContacts - message.contact)
                    }
                }
            }
        }
    }

    fun onAction(action: Action) = coroutineScopeInstance.launch {
        when (action) {
            is Action.OnContactClicked -> {
                newsFlowCollector.emit(
                    News.ContactClicked(action.contact)
                )
            }

            is Action.OnContactLongPressed -> {
                newsFlowCollector.emit(
                    News.ContactLongPressed(action.contact)
                )
            }
        }
    }

    @Composable
    override fun Render() {
        ContactListPartContent(
            component = this
        )
    }

    sealed interface News {
        class ContactClicked(
            val contact: Contact
        ) : News

        class ContactLongPressed(
            val contact: Contact
        ) : News
    }

    sealed interface Message {
        class SelectContact(
            val contact: Contact
        ) : Message

        class UnselectContact(
            val contact: Contact
        ) : Message

        class ContactListUpdated(
            val contacts: List<Contact>
        ) : Message
    }

    sealed interface Action {
        class OnContactLongPressed(
            val contact: Contact
        ) : Action

        class OnContactClicked(
            val contact: Contact
        ) : Action
    }

    @Immutable
    data class ContactsListState(
        val contacts: List<Contact>,
        val selectedContacts: List<Contact>
    )
}
