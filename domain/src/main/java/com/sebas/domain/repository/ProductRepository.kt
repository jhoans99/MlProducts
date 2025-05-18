package com.sebas.domain.repository

import com.sebas.core.common.Result
import com.sebas.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun fetchProductsByQuery(query: String, pageSize: Int,skip: Int): Flow<Result<List<Product>>>


    suspend fun fetchDetailsProduct(id: String): Flow<Result<Product>>


    suspend fun saveRecentViewedProduct(id: String)
    suspend fun getRecentViewedProduct(): String?
}