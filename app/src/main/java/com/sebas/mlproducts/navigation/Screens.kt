package com.sebas.mlproducts.navigation

sealed class Screens(val route: String) {
    object SearchProduct: Screens("searchProduct")

    object ResultProduct : Screens("result?query={query}") {
        fun createRoute(query: String) = "result?query=$query"
    }
    object DetailProduct : Screens("detail/{id}") {
        fun createRoute(id: String) = "detail/$id"
    }
}