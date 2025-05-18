package com.sebas.searchproducts.ui.search

import com.sebas.core.common.Result
import com.sebas.domain.model.Product
import com.sebas.domain.repository.ProductRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchProductViewModelTest  {
    private val dispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)

    @MockK
    private lateinit var productRepository: ProductRepository



    private lateinit var viewModelTest: SearchProductViewModel

    @Before
    fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(dispatcher)
        viewModelTest = SearchProductViewModel(productRepository)
    }

    @After
    fun after() {
        unmockkAll()
        Dispatchers.resetMain()
    }


    @Test
    fun shouldUpdateQueryValueWhenCallOnUpdateQueryValue() {

        val valueChange = "iphone"

        viewModelTest.onUpdateQueryValue(valueChange)

        assertEquals(valueChange,viewModelTest.uiState.value.query)
    }

    @Test
    fun should_ValueShowToast_when_call_onUpdateValueShowToast() {
        val valueChanged = true

        viewModelTest.onUpdateValueShowToast(valueChanged)

        assertEquals(valueChanged,viewModelTest.uiState.value.isShowToastEmptyQuery)
    }

    @Test
    fun onValidateSearchText_triggers_show_toast_when_query_is_empty(){
        viewModelTest.onUpdateQueryValue("")

        viewModelTest.onValidateSearchText {  }

        assertEquals(true,viewModelTest.uiState.value.isShowToastEmptyQuery)
    }

    @Test
    fun onValidateSearchText_not_triggers_show_toast_when_query_is_not_empty(){
        viewModelTest.onUpdateQueryValue("iphone")

        viewModelTest.onValidateSearchText {  }

        assertEquals(false,viewModelTest.uiState.value.isShowToastEmptyQuery)
    }

    @Test
    fun getIdRecentProduct_calls_fetchProductRecentViewed_when_id_is_not_null() {
        scope.runTest {
            coEvery { productRepository.getRecentViewedProduct() } returns "123"
            coEvery { productRepository.fetchDetailsProduct("123") } returns flow {
                emit(com.sebas.core.common.Result.Success(Product()))
            }

            viewModelTest.getIdRecentProduct()
            runCurrent()

            assertEquals(Product(), viewModelTest.uiState.value.detailProduct)
        }
    }

    @Test
    fun fetchProductRecentViewed_updates_uiState_on_success() {
        scope.runTest {
            coEvery { productRepository.fetchDetailsProduct("123") } returns flow {
                emit(Result.Success(Product()))
            }

            viewModelTest.fetchProductRecentViewed("123")


            assertEquals(Product(), viewModelTest.uiState.value.detailProduct)
        }
    }
}