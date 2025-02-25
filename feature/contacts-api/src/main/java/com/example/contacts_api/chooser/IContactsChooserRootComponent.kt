package com.example.contacts_api.chooser

import com.example.contacts_api.models.Contact
import kotlinx.coroutines.flow.FlowCollector
import su.ivcs.one.navigation.BaseComponent

interface IContactsChooserRootComponent : BaseComponent {

    val newsFlowCollector: FlowCollector<News>

    sealed interface News {
        class ContactsSelected(
            val contacts: List<Contact>,
        ) : News

        object CancelClicked : News
    }
}