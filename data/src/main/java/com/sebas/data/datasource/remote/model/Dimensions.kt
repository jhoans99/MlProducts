package com.sebas.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class Dimensions(
    @SerializedName("width")
    val width: Float = 0f,
    @SerializedName("height")
    val height:  Float = 0f,
    @SerializedName("depth")
    val depth: Float = 0f
)
