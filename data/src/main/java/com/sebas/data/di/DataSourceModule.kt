package com.sebas.data.di

import com.sebas.data.datasource.LocalDataSource
import com.sebas.data.datasource.ProductDataSource
import com.sebas.data.datasource.impl.LocalDataSourceImpl
import com.sebas.data.datasource.impl.ProductDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindProductDataSource(
        productDataSourceImpl: ProductDataSourceImpl
    ): ProductDataSource

    @Binds
    abstract fun bindLocalDataSource(
        localDataSourceImpl: LocalDataSourceImpl
    ): LocalDataSource
}