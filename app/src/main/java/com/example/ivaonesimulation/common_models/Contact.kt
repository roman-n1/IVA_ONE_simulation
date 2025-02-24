package com.example.ivaonesimulation.common_models

import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
)