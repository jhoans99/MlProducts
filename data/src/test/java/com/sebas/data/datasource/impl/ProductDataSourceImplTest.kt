package com.sebas.data.datasource.impl

import com.sebas.data.datasource.ProductDataSource
import com.sebas.data.datasource.remote.ProductApiService
import com.sebas.data.datasource.remote.model.ProductsDto
import com.sebas.data.datasource.remote.model.ProductsItem
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response


@OptIn(ExperimentalCoroutinesApi::class)
class ProductDataSourceImplTest {

    private val dispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)

    @MockK
    private lateinit var apiService: ProductApiService

    private lateinit var dataSource: ProductDataSourceImpl


    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun =  true)
        Dispatchers.setMain(dispatcher)
        dataSource = ProductDataSourceImpl(apiService)
    }

    @After
    fun after() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun fetchProductByQuery_returns_list_when_response_is_successful() {
        scope.runTest {
            val responseDto: ProductsDto = mockk()

            every { responseDto.products } returns listOf(mockk())

            coEvery {
                apiService.fetchProductByQuery("",0,0)
            } returns Response.success(responseDto)

            val result = dataSource.fetchProductByQuery("", 0, 0)

            assertEquals(responseDto.products,result)

            coVerify(exactly = 1) {
                apiService.fetchProductByQuery("",0 ,0)
            }
        }
    }

    @Test(expected = Exception::class)
    fun fetchProductByQuery_return_throws_exception_when_body_is_null()  {
        scope.runTest {
            coEvery {
                apiService.fetchProductByQuery(any(), any(), any())
            } returns Response.success(null)

            dataSource.fetchProductByQuery("", 0, 0)

            coVerify(exactly = 1) {
                apiService.fetchProductByQuery("",0 ,0)
            }
        }

    }

    @Test(expected = Exception::class)
    fun fetchProductByQuery_throws_exception_when_response_is_not_successful() {
        scope.runTest {
            coEvery {
                apiService.fetchProductByQuery(any(), any(), any())
            } returns Response.error(404, ResponseBody.create(null, "Not found"))

            dataSource.fetchProductByQuery("", 0, 0)

            coVerify(exactly = 1) {
                apiService.fetchProductByQuery("",0 ,0)
            }
        }

    }

    @Test
    fun fetchDetailsProduct_returns_item_when_response_is_successful() {
        scope.runTest {
            val responseExpected = ProductsItem()
            coEvery {
                apiService.fetchDetailsProduct(any())
            } returns Response.success(responseExpected)

            val result = dataSource.fetchDetailsProduct("1")

            assertEquals(responseExpected,result)

            coVerify(exactly = 1) {
                apiService.fetchDetailsProduct("1")
            }
        }
    }

    @Test(expected = Exception::class)
    fun fetchDetailsProduct_throws_exception_when_body_is_null() {
        scope.runTest {
            coEvery {
                apiService.fetchDetailsProduct(any())
            } returns Response.success(null)

            dataSource.fetchDetailsProduct("1")

            coVerify(exactly = 1) {
                apiService.fetchDetailsProduct("1")
            }
        }
    }

    @Test(expected = Exception::class)
    fun fetchDetailsProduct_throws_exception_when_response_is_not_successful() {
        scope.runTest {
            coEvery {
                apiService.fetchDetailsProduct(any())
            } returns Response.error(403, ResponseBody.create(null, "Forbidden"))

            dataSource.fetchDetailsProduct("0")

            coVerify(exactly = 1) {
                apiService.fetchDetailsProduct("1")
            }
        }
    }
}
