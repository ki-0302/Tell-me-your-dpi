package com.maho_ya.tell_me_your_dpi.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.maho_ya.tell_me_your_dpi.R

private val LightThemeColors = lightColors(
    primary = Colors.RedD01C31,
    primaryVariant = Colors.RedD01C31, // AppBar & System Bar(status bar&navigation bar)
    onPrimary = Colors.PinkE2E0E1, // コンテンツ色
    secondaryVariant = Colors.Red48282C,
    onSecondary = Colors.PinkE2E0E1,
    background = Colors.GrayF0F0F3, // スクロール可能なコンテンツの背景色
    onBackground = Colors.Blue202223,
)

private val DarkThemeColors = darkColors(
    primary = Colors.Red211314,
    primaryVariant = Colors.Red211314,
    onPrimary = Colors.PinkE2E0E1,
    secondaryVariant = Colors.GrayB8B8B8,
    onSecondary = Colors.RedD11C2C,
    background = Colors.Gray333,
    onBackground = Color.White
)

private val AppImages = Images(
    logo = R.drawable.ic_logo_toolbar,
    contentCopy = R.drawable.ic_content_copy,
    copyFab = R.drawable.ic_content_copy
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // https://developer.android.com/jetpack/compose/themes/anatomy#theme-function
    // contentsに指定されたComposable内で引数に指定した値が提供されるようになる
    // ここではLocalImages経由で提供する画像のリソースIDが使用できるようになる
    // darkThemeとLightThemeで値を切り替えしやすいなどの利点がある
    CompositionLocalProvider(
        LocalImages provides AppImages
    ) {
        MaterialTheme(
            colors = if (darkTheme) DarkThemeColors else LightThemeColors,
            typography = AppTypography,
            shapes = AppShapes,
            content = content
        )
    }
}

// https://developer.android.com/jetpack/compose/themes/anatomy#theme-object
// Theme関数と同じ名前をつけるのが慣習
object AppTheme {
    // 階層で最も近いCompositionLocalProviderから取得する値を返す。ここではAppTheme関数で定義されたロゴが返る
    val images: Images
        @Composable
        get() = LocalImages.current
}
