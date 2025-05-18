package com.sebas.mlproducts.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sebas.detailproduct.ui.DetailProductRoute
import com.sebas.resultproducts.ui.ResultProductRoute
import com.sebas.searchproducts.ui.search.SearchProductRoute

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "searchProduct") {
        composable("searchProduct") {
            SearchProductRoute(
                onNavigateResult = { query ->
                    navController.navigate("resultProduct/${query}")
                },
                onNavigateDetail = { idProduct ->
                    navController.navigate("detailProduct/${idProduct}")
                }
            )
        }

        composable(
            "resultProduct/{query}",
            arguments = listOf(
                navArgument("query") { type = NavType.StringType }
            )
        ) {
            ResultProductRoute(
                onNavigateToDetails = { idProduct ->
                    navController.navigate("detailProduct/${idProduct}")
                },
                onNavigationBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            "detailProduct/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            )
        ) {
            DetailProductRoute(
                onNavigationBack = {
                    navController.popBackStack()
                },
                onNavigateSearchProduct = {
                    navController.navigate("searchProduct")
                }
            )
        }
    }
}