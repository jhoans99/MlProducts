package com.sebas.mlproducts.ui

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.sebas.mlproducts.navigation.AppNavigation
import com.sebas.mlproducts.ui.theme.MlProductsTheme

@Composable
fun App() {
    MlProductsTheme {
        SetStatusBar()
        SetContent()
    }
}


@Composable
fun SetStatusBar() {
    val view = LocalView.current

    SideEffect {
        val window = (view.context as Activity).window
        window.statusBarColor = Color.Transparent.toArgb()
        window.navigationBarColor = Color.Transparent.toArgb()

        val isAppearanceLight = true

        WindowCompat.getInsetsController(window,view).isAppearanceLightStatusBars = isAppearanceLight
        WindowCompat.getInsetsController(window,view).isAppearanceLightNavigationBars = isAppearanceLight
    }
}


@Composable
fun SetContent() {
    BuildNavHostContent(Modifier)
}

@Composable
fun BuildNavHostContent(modifier: Modifier) {
    Box(
      modifier
          .systemBarsPadding()
          .imePadding()
    ) {
        Scaffold(
            contentWindowInsets = WindowInsets(0,0,0,0)
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal
                        )
                    )
            ) {
                AppNavigation()
            }
        }
    }
}