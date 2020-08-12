plugins {
    id("java-platform")
    id("maven-publish")
}

val analytics = "17.4.4"
val android_browser_helper = "1.3.2"
val androidx_hilt = "1.0.0-alpha02"
val androidx_test_ext = "1.1.1"
val androidx_test_espresso = "3.2.0"
val appcompat = "1.2.0"
val constraint_layout = "1.1.3"
val core_ktx = "1.3.1"
// https://github.com/Kotlin/kotlinx.coroutines
val coroutines = "1.3.8"
val crashlytics = "17.1.1"
val junit = "4.13"
val lifecycle = "2.2.0"
val material = "1.2.0"
val mockito = "3.3.1"
val moshi = "1.9.3"
val okhttp = "4.8.0"
val oss_licenses = "17.0.0"
val perf = "19.0.8"
val play_core_ktx = "1.8.1"
val recycler_view = "1.1.0"
val recycler_view_selection = "1.1.0-rc01"
val retrofit = "2.9.0"
val timber = "4.7.1"

// Use the same dependencies in multi-module.
dependencies {
    constraints {
        api("${Libs.ANALYTICS}:$analytics")
        api("${Libs.ANDROID_BROWSER_HELPER}:$android_browser_helper")
        api("${Libs.ANDROIDX_HILT_COMPILER}:$androidx_hilt")
        api("${Libs.ANDROIDX_HILT_VIEW_MODEL}:$androidx_hilt")
        api("${Libs.ANDROIDX_TEST_EXT}:$androidx_test_ext")
        api("${Libs.ANDROIDX_TEST_ESPRESSO}:$androidx_test_espresso")
        api("${Libs.APPCOMPAT}:$appcompat")
        api("${Libs.CONSTRAINT_LAYOUT}:$constraint_layout")
        api("${Libs.CORE_KTX}:$core_ktx")
        api("${Libs.COROUTINES}:$coroutines")
        api("${Libs.COROUTINES_TEST}:$coroutines")
        api("${Libs.CRASHLYTICS}:$crashlytics")
        api("${Libs.HILT_ANDROID}:${Versions.HILT}")
        api("${Libs.HILT_COMPILER}:${Versions.HILT}")
        api("${Libs.JUNIT}:$junit")
        api("${Libs.KOTLIN_STDLIB}:${Versions.KOTLIN}")
        api("${Libs.LIFECYCLE_LIVE_DATA_KTX}:$lifecycle")
        api("${Libs.LIFECYCLE_VIEW_MODEL_KTX}:$lifecycle")
        api("${Libs.MATERIAL}:$material")
        api("${Libs.MOCKITO}:$mockito")
        api("${Libs.MOSHI}:$moshi")
        api("${Libs.MOSHI_KOTLIN}:$moshi")
        api("${Libs.MOSHI_KOTLIN_CODEGEN}:$moshi")
        api("${Libs.NAVIGATION_DYNAMIC_FEATURES_FRAGMENT}:${Versions.NAVIGATION}")
        api("${Libs.NAVIGATION_FRAGMENT_KTX}:${Versions.NAVIGATION}")
        api("${Libs.NAVIGATION_TEST}:${Versions.NAVIGATION}")
        api("${Libs.NAVIGATION_UI_KTX}:${Versions.NAVIGATION}")
        api("${Libs.OKHTTP}:$okhttp")
        api("${Libs.OKHTTP_LOGGING_INTERCEPTOR}:$okhttp")
        api("${Libs.OSS_LICENSES}:$oss_licenses")
        api("${Libs.PERF}:$perf")
        api("${Libs.PLAY_CORE_KTX}:$play_core_ktx")
        api("${Libs.RECYCLER_VIEW}:$recycler_view")
        api("${Libs.RECYCLER_VIEW_SELECTION}:$recycler_view_selection")
        api("${Libs.RETROFIT}:$retrofit")
        api("${Libs.RETROFIT_CONVERTER_MOSHI}:$retrofit")
        api("${Libs.TIMBER}:$timber")
    }
}

publishing {
    publications {
        create<MavenPublication>("myPlatform") {
            from(components["javaPlatform"])
        }
    }
}
