package com.sebas.detailproduct.ui

import androidx.lifecycle.SavedStateHandle
import com.sebas.core.common.Result
import com.sebas.detailproduct.state.DetailsProductUiState
import com.sebas.domain.model.Product
import com.sebas.domain.repository.ProductRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
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
class DetailProductViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)

    @MockK
    private lateinit var repository: ProductRepository

    @MockK
    private val savedStateHandle = mockk<SavedStateHandle>()

    private lateinit var viewModel: DetailProductViewModel

    @Before
    fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(dispatcher)
        coEvery { savedStateHandle.get<String>("id") } returns "114"
        viewModel = DetailProductViewModel(savedStateHandle,repository)
    }

    @After
    fun after() {
        unmockkAll()
        Dispatchers.resetMain()
    }


    @Test
    fun when_repository_fetchDetailsProduct_returns_loading_should_update_ui_with_loading_state() {
        scope.runTest {
            coEvery { repository.fetchDetailsProduct(any()) } returns flow {
                emit(Result.Loading)
            }

            viewModel.detailProductById()

            runCurrent()

            assertEquals(true,viewModel.uiState.value.isLoading)
        }
    }

    @Test
    fun when_repository_fetchDetailsProduct_returns_success_should_update_ui_detail_product() {
        scope.runTest {
            val mockkResponse = Product()
            coEvery { repository.fetchDetailsProduct(any()) } returns flow {
                emit(Result.Success(mockkResponse))
            }

            viewModel.detailProductById()

            runCurrent()

            assertEquals(DetailsProductUiState(detailProduct = mockkResponse),viewModel.uiState.value)
        }
    }

    @Test
    fun when_repository_fetchDetailsProduct_returns_error_then_ui_should_show_modal_error() {
        scope.runTest {
            coEvery { repository.fetchDetailsProduct(any()) } returns flow {
                emit(Result.Error("Ha ocurrido un error"))
            }

            viewModel.detailProductById()

            runCurrent()

            assertEquals(DetailsProductUiState(isShowModalError = true, isLoading = false),viewModel.uiState.value)
        }
    }
}