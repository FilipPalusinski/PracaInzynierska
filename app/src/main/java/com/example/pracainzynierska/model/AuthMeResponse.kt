package com.example.pracainzynierska.model

data class AuthMeResponse(
    val avatarUrl: String,
    val createdAt: String,
    val email: String,
    val id: String,
    val isActive: Boolean,
    val name: String,
    val role: String,
    val updatedAt: String
)