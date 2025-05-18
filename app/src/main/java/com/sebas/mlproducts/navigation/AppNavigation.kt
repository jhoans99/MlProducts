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

    NavHost(navController = navController, startDestination = Screens.SearchProduct.route) {
        composable(Screens.SearchProduct.route) {
            SearchProductRoute(
                onNavigateResult = { query ->
                    navController.navigate(
                        Screens.ResultProduct.createRoute(query)
                    )
                },
                onNavigateDetail = { idProduct ->
                    navController.navigate(
                        Screens.DetailProduct.createRoute(idProduct)
                    )
                }
            )
        }

        composable(
            Screens.ResultProduct.route,
            arguments = listOf(
                navArgument("query") { type = NavType.StringType }
            )
        ) {
            ResultProductRoute(
                onNavigateToDetails = { idProduct ->
                    navController.navigate(
                        Screens.DetailProduct.createRoute(idProduct)
                    )
                },
                onNavigationBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            Screens.DetailProduct.route,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            )
        ) {
            DetailProductRoute(
                onNavigationBack = {
                    navController.popBackStack()
                },
                onNavigateSearchProduct = {
                    navController.navigate(
                        Screens.SearchProduct.route
                    )
                }
            )
        }
    }
}