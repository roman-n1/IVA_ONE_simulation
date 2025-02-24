package com.example.contacts.chooser

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.example.contacts.common.contact_list.ContactsListPartComponent
import com.example.contacts.common.toolbar.SearchToolbarComponent
import com.example.models.Contact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import su.ivcs.one.navigation.BaseComponent

class ContactsChooserListComponent(
    componentContext: ComponentContext,
    private val newsFlowCollector: FlowCollector<News>,
) : ComponentContext by componentContext, BaseComponent {

    private val coroutineScopeInstance: CoroutineScope = componentContext.coroutineScope()

    private val contactsListPartComponentNewsFlow =
        MutableSharedFlow<ContactsListPartComponent.News>()
    private val contactsListPartComponentMessageFlow =
        MutableSharedFlow<ContactsListPartComponent.Message>()

    private val contacts = List(100) { index ->
        Contact(
            id = index,
            name = "Person $index",
            email = "person$index@email.com",
            phone = "phone $index"
        )
    }

    private val searchToolbarComponent = SearchToolbarComponent(
        componentContext = componentContext,
        title = "Выбор контактов",
        onQueryChanged = { query ->
            coroutineScopeInstance.launch {
                contactsListPartComponentMessageFlow.emit(
                    ContactsListPartComponent.Message.ContactListUpdated(
                        contacts.filter {
                            it.name.contains(query)
                                    || it.email.contains(query)
                                    || it.phone.contains(query)
                        }
                    )
                )
            }
        }
    )

    private val listPartComponent = ContactsListPartComponent(
        componentContext = componentContext,
        contactsList = contacts,
        newsFlowCollector = contactsListPartComponentNewsFlow,
        messageFlow = contactsListPartComponentMessageFlow,
    )

    private val _selectedContacts =
        MutableStateFlow<List<Contact>>(emptyList()) // StateFlow для подписки
    val selectedContacts: StateFlow<List<Contact>> = _selectedContacts.asStateFlow()

    init {
        subscribeOnContactsListPartNews()
    }

    private fun subscribeOnContactsListPartNews() = coroutineScopeInstance.launch {
        contactsListPartComponentNewsFlow.collect { news ->
            when (news) {
                is ContactsListPartComponent.News.ContactClicked -> {
                    newsFlowCollector.emit(News.ContactClicked(news.contact))
                }

                is ContactsListPartComponent.News.ContactLongPressed -> {
                    selectContact(news.contact)
                }
            }
        }
    }

    private suspend fun selectContact(contact: Contact) {
        _selectedContacts.update { currentContacts ->
            if (currentContacts.contains(contact)) {
                currentContacts - contact
            } else {
                currentContacts + contact
            }
        }
        contactsListPartComponentMessageFlow.emit(
            if (_selectedContacts.value.contains(contact)) {
                ContactsListPartComponent.Message.SelectContact(contact)
            } else {
                ContactsListPartComponent.Message.UnselectContact(contact)
            }
        )
    }


    @Composable
    override fun Render() {
        val selectedContactsState by selectedContacts.collectAsState() // Подписка на selectedContacts

        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                searchToolbarComponent.Render()
                listPartComponent.Render()
            }

            // FAB с галочкой, если выбран хотя бы один контакт
            if (selectedContactsState.isNotEmpty()) {
                FloatingActionButton(
                    onClick = { confirmSelection() },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Подтвердить выбор",
                        tint = Color.White
                    )
                }
            }
        }
    }

    private fun confirmSelection() = coroutineScopeInstance.launch {
        newsFlowCollector.emit(
            News.ContactsSelected(_selectedContacts.value)
        )
    }

    sealed interface News {
        class ContactClicked(val contact: Contact) : News
        class ContactsSelected(val contacts: List<Contact>) : News
    }
}