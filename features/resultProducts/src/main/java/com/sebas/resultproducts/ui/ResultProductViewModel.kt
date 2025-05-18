package com.sebas.resultproducts.ui

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sebas.core.common.Result
import com.sebas.domain.repository.ProductRepository
import com.sebas.resultproducts.state.ResultProductUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ResultProductViewModel @Inject constructor(
    savedState: SavedStateHandle,
    private val repository: ProductRepository
): ViewModel() {

    private var _uiState = MutableStateFlow(ResultProductUiState())
    val uiState: StateFlow<ResultProductUiState> = _uiState

    private val query = savedState.get<String>("query") ?: ""
    private var currentPage = 0
    private val pageSize = 8
    private var hasMore = true

    fun onLoadInitData() {
        _uiState.value = _uiState.value.copy(
            query = query
        )
    }

    fun fetchProducts() {
        if(_uiState.value.isLoading || !hasMore) return
        val skip = currentPage * pageSize

        viewModelScope.launch {
                repository.fetchProductsByQuery(query, pageSize, skip).collect { result ->
                    when(result) {
                        is Result.Error -> {
                            onUpdateShowModalError(true)
                            _uiState.value = _uiState.value.copy(
                                isLoading = false
                            )
                        }
                        Result.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
                        is Result.Success -> {
                            _uiState.update {
                                it.copy(
                                    listProduct = it.listProduct + result.data,
                                    isLoading = false
                                )
                            }
                            hasMore = result.data.isNotEmpty()
                            if (hasMore) currentPage++
                        }
                    }
                }
        }
    }

    fun onUpdateShowModalError(value: Boolean) {
        _uiState.value = _uiState.value.copy(showModalError = value)
    }

    @VisibleForTesting
    fun setLoadingForTest(value: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = value)
    }
}