package com.example.ivaonesimulation.features.contacts.list

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.example.ivaonesimulation.ComponentRetainedInstance
import com.example.ivaonesimulation.common_models.Contact
import com.example.ivaonesimulation.features.contacts.common.contact_list.ContactsListPartComponent
import com.example.ivaonesimulation.features.contacts.common.toolbar.DefaultToolbarComponent
import com.example.ivaonesimulation.features.contacts.common.toolbar.SelectToolbarComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.serialization.Serializable
import su.ivcs.one.navigation.BaseComponent
import su.ivcs.one.navigation.ScreenComponent

class ContactListComponent(
    componentContext: ComponentContext,
    private val newsFlowCollector: FlowCollector<News>,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ScreenComponent,
    ComponentContext by componentContext {

    private val coroutineScopeInstance =
        instanceKeeper.getOrCreate { ComponentRetainedInstance(ioDispatcher) }

    private val contactsListPartComponentNewsFlow = MutableSharedFlow<ContactsListPartComponent.News>()
    private val contactsListPartComponentMessageFlow = MutableSharedFlow<ContactsListPartComponent.Message>()
    private val selectToolbarMessageFlow = MutableSharedFlow<SelectToolbarComponent.Message>()

    private val toolbarNavigation = SlotNavigation<ToolbarConfig>()

    val toolbarSlot = childSlot(
        source = toolbarNavigation,
        serializer = ToolbarConfig.serializer(),
        initialConfiguration = { ToolbarConfig.Default },
        key = "toolbar_navigation",
        handleBackButton = false,
        childFactory = ::createToolbarChild
    )

    private var isSelectMode = false

    init {
        subscribeOnContactsListPartNews()
    }

    private fun subscribeOnContactsListPartNews() = coroutineScopeInstance {
        contactsListPartComponentNewsFlow.collect { news ->
            when (news) {
                is ContactsListPartComponent.News.ContactClicked -> {
                    newsFlowCollector.emit(
                        News.ContactClicked(
                            contact = news.contact,
                        )
                    )
                }

                is ContactsListPartComponent.News.ContactLongPressed -> {
                    selectContact(news.contact)
                }
            }
        }
    }

    private val selectedContacts = mutableListOf<Contact>()

    private suspend fun selectContact(contact: Contact) {
        isSelectMode = true
        toolbarNavigation.navigate(
            transformer = { if (isSelectMode) ToolbarConfig.SelectMode else ToolbarConfig.Default },
            onComplete = { _, _ -> /*do nothing*/ },
        )
        contactsListPartComponentMessageFlow.emit(
            ContactsListPartComponent.Message.SelectContact(
                contact
            )
        )
        if (selectedContacts.contains(contact)) {
            selectedContacts.remove(contact)
        } else {
            selectedContacts.add(contact)
        }
        selectToolbarMessageFlow.emit(
            SelectToolbarComponent.Message.SelectedItemsCountChanged(
                count = selectedContacts.size
            )
        )
    }

    private fun createToolbarChild(
        toolbarConfig: ToolbarConfig,
        componentContext: ComponentContext
    ): BaseComponent = when (toolbarConfig) {
        ToolbarConfig.Default -> DefaultToolbarComponent(
            componentContext = componentContext,
            title = "Contacts",
        )

        ToolbarConfig.SelectMode -> SelectToolbarComponent(
            componentContext = componentContext,
            messageFlow = selectToolbarMessageFlow,
        )
    }


    val listPartComponent = ContactsListPartComponent(
        componentContext = componentContext,
        contactsList = getContacts(),
        newsFlowCollector = contactsListPartComponentNewsFlow,
        messageFlow = contactsListPartComponentMessageFlow,
    )

    private fun getContacts(): List<Contact> =
        List(100) { index ->
            Contact(
                id = index,
                name = "Person $index",
                email = "email $index",
                phone = "phone $index"
            )
        }

    @Composable
    override fun Render() {
        ContactListContent(
            contactListComponent = this,
        )
    }

    @Serializable
    sealed interface ToolbarConfig {
        @Serializable
        data object Default : ToolbarConfig

        @Serializable
        data object SelectMode : ToolbarConfig
    }

    sealed interface News {
        class ContactClicked(
            val contact: Contact
        ) : News
    }
}