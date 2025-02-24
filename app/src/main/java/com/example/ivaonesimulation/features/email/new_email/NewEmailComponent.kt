package com.example.ivaonesimulation.features.email.new_email

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.example.ivaonesimulation.ComponentRetainedInstance
import com.example.ivaonesimulation.common_models.Contact
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import su.ivcs.one.navigation.ScreenComponent

class NewEmailComponent(
    componentContext: ComponentContext,
    private val newsFlowCollector: FlowCollector<News>,
    private val messageFlow: Flow<Message>,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ScreenComponent,
    ComponentContext by componentContext {

    private val coroutineScopeInstance =
        instanceKeeper.getOrCreate { ComponentRetainedInstance(ioDispatcher) }

    private val _selectedContacts =
        MutableStateFlow<List<Contact>>(emptyList()) // StateFlow для подписки
    val selectedContacts: StateFlow<List<Contact>> = _selectedContacts.asStateFlow()

    init {
        subscribeOnMessageFlow()
    }

    private fun subscribeOnMessageFlow() = coroutineScopeInstance {
        messageFlow.collect { message ->
            when (message) {
                is Message.ContactsSelected -> {
                    _selectedContacts.value = message.contacts
                }
            }
        }
    }

    fun onAction(action: Action) = coroutineScopeInstance {
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