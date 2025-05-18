package com.sebas.data.datasource.remote

import com.sebas.data.datasource.remote.model.ProductsDto
import com.sebas.data.datasource.remote.model.ProductsItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {

    @GET("/products/search")
    suspend fun fetchProductByQuery(
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): Response<ProductsDto>

    @GET("/products/{id}")
    suspend fun fetchDetailsProduct(
        @Path("id")
        id: String
    ): Response<ProductsItem>
}