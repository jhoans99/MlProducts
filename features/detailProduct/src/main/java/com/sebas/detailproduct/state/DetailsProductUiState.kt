package com.sebas.detailproduct.state

import com.sebas.domain.model.Product

data class DetailsProductUiState(
    val isLoading: Boolean = false,
    val detailProduct: Product = Product(),
    val isShowModalError: Boolean = false
)
