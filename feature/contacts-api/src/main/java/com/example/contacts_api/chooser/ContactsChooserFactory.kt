package com.example.contacts_api.chooser

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.FlowCollector

interface ContactsChooserFactory {
    fun create(
        componentContext: ComponentContext,
        newsFlowCollector: FlowCollector<IContactsChooserRootComponent.News>
    ): IContactsChooserRootComponent
}