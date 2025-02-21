package com.example.ivaonesimulation.features.contacts.chooser

import com.example.ivaonesimulation.common_models.Contact
import kotlinx.coroutines.flow.FlowCollector
import su.ivcs.one.navigation.BaseComponent

interface IContactsChooserRootComponent  : BaseComponent {

    val newsFlowCollector: FlowCollector<News>

    sealed interface News {
        class ContactsSelected(
            val contacts: List<Contact>,
        ) : News

        object CancelClicked : News
    }
}