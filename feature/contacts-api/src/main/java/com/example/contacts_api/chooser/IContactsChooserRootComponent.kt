package com.example.contacts_api.chooser

import com.example.contacts_api.models.Contact
import su.ivcs.one.navigation.BaseComponent

interface IContactsChooserRootComponent : BaseComponent {

    sealed interface News {
        class ContactsSelected(
            val contacts: List<Contact>,
        ) : News

        object CancelClicked : News
    }
}