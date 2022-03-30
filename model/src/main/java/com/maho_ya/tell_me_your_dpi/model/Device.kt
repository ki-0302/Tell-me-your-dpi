package com.maho_ya.tell_me_your_dpi.model

data class Device(
    var densityQualifier: String,
    var densityDpi: Int,
    var realDisplaySizeWidth: Int,
    var realDisplaySizeHeight: Int,
    var brand: String,
    var model: String,
    var apiLevel: Int,
    var androidOsVersion: String,
    var androidCodeName: String,
    var totalMemory: Int,
    var availableMemory: Int
)
