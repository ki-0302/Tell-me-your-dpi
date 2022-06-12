package com.maho_ya.tell_me_your_dpi.util

import kotlinx.coroutines.flow.SharingStarted

private const val StopTimeoutMillis: Long = 5000

// 購読されている場合に開始。購読されなくなると開放される
val WhileUiSubscribed: SharingStarted = SharingStarted.WhileSubscribed(StopTimeoutMillis)
