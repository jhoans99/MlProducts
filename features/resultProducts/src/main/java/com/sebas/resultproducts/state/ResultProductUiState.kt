package com.sebas.resultproducts.state

import com.sebas.domain.model.Product

data class ResultProductUiState(
    val isLoading: Boolean = false,
    val listProduct: List<Product> = emptyList(),
    val showModalError: Boolean = false,
    val query: String = ""
)
