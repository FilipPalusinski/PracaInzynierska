package com.example.pracainzynierska.model

import java.io.Serializable

data class User(
    val avatarUrl: Any,
    val createdAt: String,
    val email: String,
    val id: String,
    val isActive: Boolean,
    val name: String,
    val role: String,
    val updatedAt: String
) : Serializable