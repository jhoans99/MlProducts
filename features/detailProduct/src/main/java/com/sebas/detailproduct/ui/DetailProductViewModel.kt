package com.sebas.detailproduct.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sebas.core.common.Result
import com.sebas.detailproduct.state.DetailsProductUiState
import com.sebas.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository
): ViewModel() {

    private var _uiState = MutableStateFlow(DetailsProductUiState())
    val uiState: StateFlow<DetailsProductUiState> = _uiState

    private val idProduct = savedStateHandle.get<String>("id") ?: ""

    fun detailProductById() {
        viewModelScope.launch { 
            productRepository.fetchDetailsProduct(idProduct).collect {
                _uiState.value = when(it) {
                    is Result.Error -> {
                        onUpdateValueModalError(true)
                        _uiState.value.copy(isLoading = false)
                    }
                    Result.Loading -> {
                        _uiState.value.copy(isLoading = true)
                    }
                    is Result.Success -> {
                        _uiState.value.copy(
                            isLoading = false,
                            detailProduct = it.data
                        )
                    }
                }
            }
        }
    }

    fun onUpdateValueModalError(value: Boolean) {
        _uiState.value = _uiState.value.copy(isShowModalError = value)
    }

    fun saveRecentViewedProduct() {
        viewModelScope.launch {
            productRepository.saveRecentViewedProduct(idProduct)
        }
    }


}