package com.sebas.core.ui.components


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DialogComponent(
    title: String,
    message: String,
    isCancelAction: Boolean = false,
    textConfirmButton: String = "Confirmar",
    textDismissButton: String = "Cancelar",
    onConfirmAction: () -> Unit,
    onDismissAction: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismissAction() },
        title = {
            Text(title)
        },
        text = {
            Text(message)
        },
        confirmButton = {
            TextButton(
                Modifier,
                textConfirmButton
            ) {
                onConfirmAction()
            }

        },
        dismissButton = {
            when {
                isCancelAction -> {
                    TextButton(
                        Modifier,
                        "Cancelar"
                    ) {
                        onDismissAction()
                    }

                }
            }

        }
    )
}