package com.sebas.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class ProductsItem(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("category")
    val category: String = "",
    @SerializedName("price")
    val price: Float = 0f,
    @SerializedName("discountPercentage")
    val discountPercentage: Float = 0f,
    @SerializedName("rating")
    val rating: Float = 0f,
    @SerializedName("stock")
    val stock: Int = 0,
    @SerializedName("tags")
    val tags: List<String> = emptyList(),
    @SerializedName("brand")
    val brand: String? = null,
    @SerializedName("sku")
    val sku: String = "",
    @SerializedName("weight")
    val weight: Int = 0,
    @SerializedName("dimensions")
    val dimensions: Dimensions = Dimensions(),
    @SerializedName("warrantyInformation")
    val warrantyInformation: String = "",
    @SerializedName("shippingInformation")
    val shippingInformation: String = "",
    @SerializedName("availabilityStatus")
    val availabilityStatus: String = "",
    @SerializedName("reviews")
    val reviews: List<Reviews> = emptyList(),
    @SerializedName("returnPolicy")
    val returnPolicy: String  = "",
    @SerializedName("minimumOrderQuantity")
    val minimumOrderQuantity: Int = 0,
    @SerializedName("images")
    val images: List<String> = emptyList()
)
