package com.example.ivaonesimulation.features.contacts.common.toolbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.arkivanov.decompose.ComponentContext
import su.ivcs.one.navigation.BaseComponent

class SearchToolbarComponent(
    componentContext: ComponentContext,
    val title: String,
    val onQueryChanged: (String) -> Unit // Колбэк для обновления query
) : ComponentContext by componentContext,
    BaseComponent {

    @Composable
    override fun Render() {
        SearchScreen()
    }

    @Composable
    fun SearchScreen() {
        var query by remember { mutableStateOf("") }
        var isSearching by remember { mutableStateOf(false) }

        Column {
            SearchTopAppBar(
                query = query,
                onQueryChange = {
                    query = it
                    onQueryChanged(it) // Вызываем колбэк при изменении текста
                },
                isSearching = isSearching,
                onSearchClick = { isSearching = true },
                onCloseClick = {
                    isSearching = false
                    query = ""
                    onQueryChanged("") // Обнуляем состояние в колбэке
                }
            )
            // Основное содержимое экрана
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SearchTopAppBar(
        query: String,
        onQueryChange: (String) -> Unit,
        isSearching: Boolean,
        onSearchClick: () -> Unit,
        onCloseClick: () -> Unit
    ) {
        if (isSearching) {
            // Развёрнутый режим с полем ввода
            TopAppBar(
                title = {
                    TextField(
                        value = query,
                        onValueChange = onQueryChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Поиск...") },
                        singleLine = true,
                        textStyle = TextStyle(color = Color.White),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.DarkGray,
                            unfocusedContainerColor = Color.DarkGray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White
                        ),
                        trailingIcon = {
                            IconButton(onClick = onCloseClick) {
                                Icon(Icons.Default.Close, contentDescription = "Закрыть", tint = Color.White)
                            }
                        }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.LightGray)
            )
        } else {
            // Свёрнутый режим с иконкой поиска
            TopAppBar(
                title = { Text(title, color = Color.White) },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(Icons.Default.Search, contentDescription = "Поиск", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.LightGray)
            )
        }
    }
}