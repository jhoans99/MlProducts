package com.sebas.data.datasource

interface LocalDataSource {
    suspend fun saveRecentViewedProduct(id: String)
    suspend fun getRecentViewedProduct(): String?
}