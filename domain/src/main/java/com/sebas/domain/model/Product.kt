package com.sebas.domain.model

data class Product(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val price: Float = 0.0f,
    val rating: Float = 0.0f,
    val stock: Int = 0,
    val images: List<String> = emptyList(),
    val brand: String = ""
)
