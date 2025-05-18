package com.sebas.data.di

import com.sebas.data.datasource.remote.ProductApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {

    @Provides
    fun provideProductApiService(
        retrofit: Retrofit
    ): ProductApiService {
        return  retrofit.create(ProductApiService::class.java)
    }

}