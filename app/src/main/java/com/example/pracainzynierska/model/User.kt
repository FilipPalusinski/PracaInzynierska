package com.example.pracainzynierska.model

import java.io.Serializable

data class User(
    val archivedAt: Any,
    val avatarUrl: Any,
    val createdAt: String,
    val email: String,
    val id: String,
    val isActive: Boolean,
    val name: String,
    val role: Role,
    val roleId: Int,
    val updatedAt: String
) : Serializable