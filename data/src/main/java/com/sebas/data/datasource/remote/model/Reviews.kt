package com.sebas.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class Reviews(
    @SerializedName("rating")
    val rating: Float,
    @SerializedName("comment")
    val comment: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("reviewerName")
    val reviewerName: String,
    @SerializedName("reviewerEmail")
    val reviewerEmail: String
)
