package com.sebas.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sebas.core.ui.components.TextButton
import com.sebas.core.ui.theme.Blue
import com.sebas.core.ui.theme.BlueLight


@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    textButton: String,
    isEnable: Boolean = false,
    onClickAction: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = isEnable,
        shape = RoundedCornerShape(12.dp),
        onClick = {
            onClickAction()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Blue
        )
    ) {
        Text(textButton)
    }
}

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    textButton: String,
    isEnable: Boolean = false,
    onClickAction: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = isEnable,
        shape = RoundedCornerShape(12.dp),
        onClick = {
            onClickAction()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = BlueLight,
            contentColor = Blue
        )
    ) {
        Text(textButton)
    }
}

@Composable
fun TextButton(
    modifier: Modifier = Modifier,
    textButton: String,
    onClickAction: () -> Unit
) {
    TextButton(
        onClick = {
            onClickAction()
        },
        modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Blue
        )
    ) {
        Text(textButton)
    }
}






@Preview(showBackground = true)
@Composable
fun ButtonsComponentPreview() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        PrimaryButton(
            textButton = "Enviar",
            isEnable = true
        ) {  }

        SecondaryButton(
            textButton = "Enviar",
            isEnable = true
        ) {  }

        TextButton(
            textButton =  "Buton de texto"
        ) {

        }
    }
}