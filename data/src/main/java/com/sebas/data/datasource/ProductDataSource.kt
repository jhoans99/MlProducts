package com.sebas.data.datasource

import com.sebas.data.datasource.remote.model.ProductsItem

interface ProductDataSource {
    suspend fun fetchProductByQuery(query: String, pageSize: Int, skip: Int): List<ProductsItem>

    suspend fun fetchDetailsProduct(id: String): ProductsItem
}