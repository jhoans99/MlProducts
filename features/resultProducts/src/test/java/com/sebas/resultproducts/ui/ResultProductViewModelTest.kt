package com.sebas.resultproducts.ui

import androidx.lifecycle.SavedStateHandle
import com.sebas.core.common.Result
import com.sebas.domain.model.Product
import com.sebas.domain.repository.ProductRepository
import com.sebas.resultproducts.state.ResultProductUiState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
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
class ResultProductViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)

    @MockK
    private val saveStateHandle = mockk<SavedStateHandle>()

    @MockK
    private lateinit var repository: ProductRepository

    private lateinit var viewModel: ResultProductViewModel


    @Before
    fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(dispatcher)
        coEvery { saveStateHandle.get<String>("query") } returns "iphone"

        viewModel = ResultProductViewModel(saveStateHandle,repository)
    }

    @After
    fun after() {
        unmockkAll()
        Dispatchers.resetMain()
    }


    @Test
    fun onLoadInitData_update_query_from_saved_state() {
        scope.runTest {
            viewModel.onLoadInitData()
            assertEquals("iphone", viewModel.uiState.value.query)
        }
    }

    @Test
    fun fetchProducts_loads_data_successfully() {
        scope.runTest {
            coEvery {
                repository.fetchProductsByQuery(any(),any(),any())
            } returns flow {
                emit(
                    Result.Success(listOf(
                        Product(
                            title = "iphone"
                        ),
                        Product(
                            title = "iphone"
                        )
                    ))
                )
            }

            viewModel.fetchProducts()

            runCurrent()

            val state = viewModel.uiState.value
            assertEquals(false, state.isLoading)
            assertEquals("iphone", state.listProduct[0].title)
        }
    }

    @Test
    fun fetchProducts_does_not_load_if_already_loading() {
        scope.runTest {
            viewModel.setLoadingForTest(true)

            viewModel.fetchProducts()

            coVerify(exactly = 0) {
                repository.fetchProductsByQuery(any(), any(), any())
            }
        }
    }

    @Test
    fun fetchProducts_does_not_load_when_hasMore_is_false() {
        scope.runTest {
            coEvery {
                repository.fetchProductsByQuery(any(),any(),any())
            } returns flow {
                emit(Result.Success(emptyList()))
            }

            viewModel.fetchProducts()
            runCurrent()

            viewModel.fetchProducts()


            coVerify(exactly = 1) {
                repository.fetchProductsByQuery(any(), any(), any())
            }
        }
    }

    @Test
    fun fetchProducts_returns_error_then_should_show_modal_error() {
        scope.runTest {
            coEvery {
                repository.fetchProductsByQuery(any(),any(),any())
            } returns flow {
                emit(Result.Error("Error de red"))
            }

            viewModel.fetchProducts()
            runCurrent()

            assertEquals(ResultProductUiState(
                isLoading = false,
                showModalError = true
            ),viewModel.uiState.value)
        }
    }



}