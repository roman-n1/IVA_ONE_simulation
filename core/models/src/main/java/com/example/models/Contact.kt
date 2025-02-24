package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
)