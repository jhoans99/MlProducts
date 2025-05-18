package com.sebas.core.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.sebas.core.ui.theme.DarkBlue
import com.sebas.core.ui.theme.Yellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarComponent(
    title: String,
    isNavigation: Boolean = true,
    action: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = DarkBlue
            )
        },
        navigationIcon =  {
            when {
                isNavigation -> {
                    IconButton(onClick = {
                        action
                    }) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowLeft,
                            contentDescription = null,
                            tint = DarkBlue
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Yellow,
            titleContentColor = DarkBlue,
            actionIconContentColor = DarkBlue,
            navigationIconContentColor = DarkBlue
        )
    )
}