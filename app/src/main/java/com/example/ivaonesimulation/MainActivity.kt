package com.example.ivaonesimulation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import com.example.ivaonesimulation.features.root.RootComponent
import com.example.ivaonesimulation.ui.theme.IVAONESimulationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Always create the root component outside Compose on the main thread
        val root =
            RootComponent(
                componentContext = defaultComponentContext(),
            )

        setContent {
            IVAONESimulationTheme {
                root.Render()
            }
        }
    }
}

