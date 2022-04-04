package com.kamikadze328.mtstetaproject.ui.resourse

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val colors = lightColors(
    onSurface = Color.White,
    primary = purple_500,
    onPrimary = white,
    primaryVariant = purple_700,
    secondary = teal_200,
    secondaryVariant = teal_700,
    onSecondary = black,
    background = background,
)

@Composable
fun BasicMTSTetaProjectTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = colors,
        content = content
    )
}