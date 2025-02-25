package com.example.contacts.chooser

import com.arkivanov.decompose.ComponentContext
import com.example.contacts_api.chooser.ContactsChooserFactory
import com.example.contacts_api.chooser.IContactsChooserRootComponent
import kotlinx.coroutines.flow.FlowCollector

class ContactsChooserFactoryImpl : ContactsChooserFactory {
    override fun create(
        componentContext: ComponentContext,
        newsFlowCollector: FlowCollector<IContactsChooserRootComponent.News>
    ): IContactsChooserRootComponent {
        return ContactsChooserRootComponent(
            componentContext = componentContext,
            newsFlowCollector = newsFlowCollector
        )
    }
}