package com.example.ivaonesimulation.features.email.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun EmailListContent(
    component: EmailListComponent,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(100) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = if (it % 2 == 0) {
                                Color.LightGray
                            } else {
                                Color.White
                            }
                        )
                ) {
                    Spacer(Modifier.height(16.dp))
                    val person = "Person $it"
                    Text(text = person)
                    Text(text = "Email text from $person")
                    Spacer(Modifier.height(16.dp))
                }
            }
        }

        FloatingActionButton(
            onClick = { component.onAction(EmailListComponent.Action.NewEmailButtonClicked) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "New Email")
        }
    }
}
