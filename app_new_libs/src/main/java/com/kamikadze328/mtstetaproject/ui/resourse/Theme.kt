package com.kamikadze328.mtstetaproject.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.kamikadze328.mtstetaproject.ui.resourse.*

val primaryVariant = purple_700
private val colors = lightColors(
    onSurface = Color.White,
    primary = purple_200,
    onPrimary = white,
    primaryVariant = primaryVariant,
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