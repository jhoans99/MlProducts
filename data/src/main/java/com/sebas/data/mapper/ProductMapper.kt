package com.sebas.data.mapper

import com.sebas.data.datasource.remote.model.ProductsItem
import com.sebas.domain.model.Product

fun ProductsItem.toDomain(): Product {
    return Product(
        id = id,
        title = title,
        description = description,
        category = category,
        price = price,
        rating = rating,
        stock = stock,
        images = images,
        brand = brand ?: ""
    )
}