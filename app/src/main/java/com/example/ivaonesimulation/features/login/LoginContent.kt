package com.example.ivaonesimulation.features.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginContent(
    tokenCallback: (String) -> Unit,
    registerClicked: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        TextField(
            value = "Login",
            onValueChange = {},
            label = { Text("Login") }
        )
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        TextField(
            value = "Password",
            onValueChange = {},
            label = { Text("Password") }
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Button(
            onClick = {
                tokenCallback("token after login")
            },
        ) {
            Text("Enter")
        }

        Spacer(modifier = Modifier.height(50.dp))

        Button(
            onClick = {
                registerClicked()
            },
        ) {
            Text("Register")
        }
    }
}