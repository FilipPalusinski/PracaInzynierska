package com.example.pracainzynierska.model

data class SaleUnassignedItem(
    val contract: Contract,
    val customer: Customer,
    val id: String,
    val item: Item,
    val others: String,
    val qa: Any,
    val rep: Any,
    val status: Status,
    val user: User
)