package com.sebas.data.datasource.impl

import android.util.Log
import com.sebas.data.common.ErrorMessages.EMPTY_DATA_ERROR
import com.sebas.data.common.ErrorMessages.SERVICE_ERROR
import com.sebas.data.datasource.ProductDataSource
import com.sebas.data.datasource.remote.ProductApiService
import com.sebas.data.datasource.remote.model.ProductsItem
import javax.inject.Inject

class ProductDataSourceImpl @Inject constructor(
   private val apiService: ProductApiService
): ProductDataSource {

    override suspend fun fetchProductByQuery(query: String, pageSize: Int, skip: Int): List<ProductsItem> {
        val response = apiService.fetchProductByQuery(query,pageSize,skip)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return body.products
            } else {
                Log.d("Error", "fetchProductByQueryApi Empty Body")
                throw Exception(EMPTY_DATA_ERROR)
            }
        } else {
            Log.d("Error", "fetchProductByQueryApi service Error ${response.code()}")
            throw Exception("$SERVICE_ERROR ${response.message()}")
        }
    }

    override suspend fun fetchDetailsProduct(id: String): ProductsItem {
        val response = apiService.fetchDetailsProduct(id)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return body
            } else {
                Log.d("Error", "fetchDetailsProductApi Empty Body")
                throw Exception(EMPTY_DATA_ERROR)
            }
        } else {
            Log.d("Error", "fetchDetailsProductApi service Error ${response.code()}")
            throw Exception("$SERVICE_ERROR ${response.message()}")
        }
    }
}