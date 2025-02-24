package com.example.ivaonesimulation.features.contacts.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ivaonesimulation.common_models.Contact

@Composable
fun ContactDetailsContent(
    component: ContactDetailsComponent,
) {
    ContactCard(
        contact = component.contact,
        onClick = null
    )
}

@Composable
fun ContactCard(
    contact: Contact,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick != null) { onClick?.invoke() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = contact.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "📧 ${contact.email}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "📞 ${contact.phone}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}