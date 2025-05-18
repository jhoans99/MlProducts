package com.sebas.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class ProductsDto(
    @SerializedName("products")
    val products: List<ProductsItem>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("skip")
    val skip: Int,
    @SerializedName("limit")
    val limit: Int
)
