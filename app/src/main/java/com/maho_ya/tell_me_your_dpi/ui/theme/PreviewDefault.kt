package com.maho_ya.tell_me_your_dpi.ui.theme

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

// annotation classに@Previewにあらかじめ共通で使用したい設定をすることによって、 各Previewの呼び出しで共通化できるようなる
// Preview側には1個は@Previewを付ける必要がるため、このクラスで定義したアノテーションだけ付けても動作しない
@Preview(device = Devices.PIXEL_2, showSystemUi = true)
annotation class PreviewDefault
