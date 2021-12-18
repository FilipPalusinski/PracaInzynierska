package com.example.pracainzynierska.model

data class Contract(
    val createdAt: String,
    val id: String,
    val length: Int,
    val plannedSignAt: String,
    val price: Int,
    val signAddress: String,
    val signedAt: Any,
    val updatedAt: String
)