package com.sebas.searchproducts.state

import com.sebas.domain.model.Product

data class SearchProductUiState(
    val query: String = "",
    val isShowToastEmptyQuery: Boolean = false,
    val detailProduct: Product? = null
)
