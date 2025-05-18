package com.sebas.searchproducts.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sebas.core.common.Result
import com.sebas.domain.repository.ProductRepository
import com.sebas.searchproducts.state.SearchProductUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
): ViewModel() {

    private var _uiState = MutableStateFlow(SearchProductUiState())
    val uiState: StateFlow<SearchProductUiState> = _uiState


    fun onUpdateQueryValue(value: String) {
        _uiState.value = _uiState.value.copy(query = value)
    }

    fun onValidateSearchText(
        onValidSearch: () -> Unit
    ) {
        if(_uiState.value.query.isEmpty()) {
            onUpdateValueShowToast(true)
        } else {
            onValidSearch()
        }
    }

    fun onUpdateValueShowToast(value: Boolean) {
        _uiState.value = _uiState.value.copy(isShowToastEmptyQuery = value)
    }

    fun getIdRecentProduct() {
        viewModelScope.launch {
            productRepository.getRecentViewedProduct()?.let {
                fetchProductRecentViewed(id = it)
            }
        }
    }

    suspend fun fetchProductRecentViewed(id: String) {
        productRepository.fetchDetailsProduct(id).collect {
             when(it) {
                is Result.Error -> Unit
                Result.Loading -> Unit
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(detailProduct = it.data)
                }
            }
        }
    }
}