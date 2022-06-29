package com.maho_ya.tell_me_your_dpi.ui.theme

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

// https://developer.android.com/jetpack/compose/themes/anatomy
// @Immutable をつけると不変な値と認識される
// 不変と認識されることにより、Composableで最適化され高速になる。
// Themeのシステムクラスとして定義するのに利用され、Themeオブジェクトから呼ばれる使い方が一般的
@Immutable
data class Images(
    @DrawableRes val logo: Int,
    @DrawableRes val contentCopy: Int,
    @DrawableRes val copyFab: Int,
    @DrawableRes val notifications: Int
)

// Composition Local を使用すると引数Composableの最上位から引数を渡さなくても静的に呼び出すことができる
// Contextの取得も内部実装で使用されている
// staticCompositionLocalOf は状態がほぼ変わらないものを設定するのに使用する。ローカルリソースなど
internal val LocalImages = staticCompositionLocalOf<Images> {
    error("Failed to get an image.")
}
