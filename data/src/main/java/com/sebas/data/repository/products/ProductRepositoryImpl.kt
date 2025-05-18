package com.sebas.data.repository.products

import android.util.Log
import com.sebas.core.common.Result
import com.sebas.data.common.ErrorMessages.DETAIL_ERROR
import com.sebas.data.common.ErrorMessages.SEARCH_ERROR
import com.sebas.data.datasource.LocalDataSource
import com.sebas.data.datasource.ProductDataSource
import com.sebas.data.mapper.toDomain
import com.sebas.domain.model.Product
import com.sebas.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val dataSource: ProductDataSource,
    private val localDataSource: LocalDataSource
): ProductRepository {

    override suspend fun fetchProductsByQuery(
        query: String,
        pageSize: Int,
        skip: Int
    ): Flow<Result<List<Product>>> = flow {
        emit(Result.Loading)
        val response = dataSource.fetchProductByQuery(query,pageSize,skip)
        val listProduct = response.map {
            it.toDomain()
        }
        emit(Result.Success(listProduct))

    }.catch {
        Log.d("Error", "fetchProductsByQuery: ${it.cause}")
        emit(Result.Error(SEARCH_ERROR))
    }

    override suspend fun fetchDetailsProduct(id: String): Flow<Result<Product>> = flow {
        emit(Result.Loading)
        val response = dataSource.fetchDetailsProduct(id)
        emit(Result.Success(response.toDomain()))
    }.catch {
        Log.d("Error", "fetchDetailsProduct: ${it.cause}")
        emit(Result.Error(DETAIL_ERROR))
    }

    override suspend fun saveRecentViewedProduct(id: String) {
        localDataSource.saveRecentViewedProduct(id)
    }

    override suspend fun getRecentViewedProduct(): String? =
        localDataSource.getRecentViewedProduct()
}