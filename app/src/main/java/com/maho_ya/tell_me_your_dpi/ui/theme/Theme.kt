package com.maho_ya.tell_me_your_dpi.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightThemeColors = lightColors(
    primary = Colors.RedD01C31,
    primaryVariant = Colors.RedD01C31, // AppBar & System Bar(status bar&navigation bar)
    onPrimary = Colors.PinkE2E0E1, // コンテンツ色
    background = Colors.GrayF0F0F3, // スクロール可能なコンテンツの背景色
    onBackground = Colors.Blue202223,
)

private val DarkThemeColors = darkColors(
    primary = Colors.Red211314,
    primaryVariant = Colors.Red211314,
    onPrimary = Colors.PinkE2E0E1,
    background = Colors.Gray333,
    onBackground = Color.White
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkThemeColors else LightThemeColors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
