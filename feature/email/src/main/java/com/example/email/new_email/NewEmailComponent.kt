package com.example.email.new_email

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.example.contacts_api.models.Contact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import su.ivcs.one.navigation.ScreenComponent

class NewEmailComponent(
    componentContext: ComponentContext,
    private val newsFlowCollector: FlowCollector<News>,
    private val messageFlow: Flow<Message>,
) : ScreenComponent,
    ComponentContext by componentContext {

    private val coroutineScopeInstance: CoroutineScope = componentContext.coroutineScope()

    private val _selectedContacts =
        MutableStateFlow<List<Contact>>(emptyList()) // StateFlow для подписки
    val selectedContacts: StateFlow<List<Contact>> = _selectedContacts.asStateFlow()

    init {
        subscribeOnMessageFlow()
    }

    private fun subscribeOnMessageFlow() = coroutineScopeInstance.launch {
        messageFlow.collect { message ->
            when (message) {
                is Message.ContactsSelected -> {
                    _selectedContacts.value = message.contacts
                }
            }
        }
    }

    fun onAction(action: Action) = coroutineScopeInstance.launch {
        when (action) {
            is Action.SelectContactsClicked -> {
                newsFlowCollector.emit(
                    News.SelectContactsClicked
                )
            }
        }
    }

    @Composable
    override fun Render() {
        NewEmailContent(
            component = this
        )
    }

    sealed interface Action {
        object SelectContactsClicked : Action
    }

    sealed interface News {
        object SelectContactsClicked : News
    }

    sealed interface Message {
        class ContactsSelected(val contacts: List<Contact>) : Message
    }
}