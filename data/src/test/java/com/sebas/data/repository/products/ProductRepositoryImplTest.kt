package com.sebas.data.repository.products

import com.sebas.core.common.Result
import com.sebas.data.common.ErrorMessages
import com.sebas.data.datasource.LocalDataSource
import com.sebas.data.datasource.ProductDataSource
import com.sebas.data.datasource.remote.model.ProductsItem
import com.sebas.data.mapper.toDomain
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductRepositoryImplTest {
    private val dispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)

    @MockK
    private lateinit var dataSource: ProductDataSource

    @MockK
    private lateinit var localDataSource: LocalDataSource

    private lateinit var repositoryImpl: ProductRepositoryImpl

    @Before
    fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(dispatcher)
        repositoryImpl = ProductRepositoryImpl(dataSource,localDataSource)
    }

    @After
    fun after() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun fetchProductsByQuery_returns_loading_and_success_when_dataSource_returns_data() {
        scope.runTest {
            val mockkResponse = listOf<ProductsItem>()
            val mockkResponseDomain = mockkResponse.map { it.toDomain() }

            coEvery { dataSource.fetchProductByQuery(any(),any(),any()) } returns mockkResponse

            val response = repositoryImpl.fetchProductsByQuery("",0,0).toList()

            assert(response.first() is Result.Loading)
            assert(response[1] is Result.Success)
            assertEquals(mockkResponseDomain, (response[1] as Result.Success).data)
        }
    }

    @Test
    fun fetchProductsByQuery_returns_loading_and_error_when_dataSource_returns_exception() {
        scope.runTest {
            coEvery { dataSource.fetchProductByQuery(any(),any(),any()) } throws RuntimeException("Error Api")

            val response = repositoryImpl.fetchProductsByQuery("",0,0).toList()

            assert(response.first() is Result.Loading)
            assert(response[1] is Result.Error)
            assertEquals(ErrorMessages.SEARCH_ERROR, (response[1] as Result.Error).message)
        }
    }


    @Test
    fun saveRecentViewedProduct_calls_LocalDataSource_with_correct_ID() {
        scope.runTest {
            coEvery { localDataSource.saveRecentViewedProduct("123") } returns Unit

            repositoryImpl.saveRecentViewedProduct("123")

            coVerify(exactly = 1) { localDataSource.saveRecentViewedProduct("123") }
        }
    }

    @Test
    fun getRecentViewedProduct_returns_value_from_LocalDataSource() {
        scope.runTest {
            coEvery { localDataSource.getRecentViewedProduct() } returns "123"

            val result = repositoryImpl.getRecentViewedProduct()

            assertEquals("123", result)
        }
    }



}