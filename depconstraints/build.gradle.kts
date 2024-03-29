plugins {
    id("java-platform")
    id("maven-publish")
}

val androidx_browser = "1.4.0"
val androidx_hilt = "1.0.0"
val appcompat = "1.5.0"
val constraint_layout = "2.1.4"
val core_ktx = "1.8.0"
// https://github.com/Kotlin/kotlinx.coroutines
val coroutines = "1.6.4"
val firebase_bom = "30.4.0"
val junit = "4.13"
val lifecycle = "2.5.1"
val material = "1.6.1"
val mockito = "4.7.0"
val moshi = "1.13.0"
val okhttp = "4.9.3"
val oss_licenses = "17.0.0"
val play_core_ktx = "1.8.1"
val play_services_ads = "21.1.0"
val recycler_view = "1.2.1"
val recycler_view_selection = "1.1.0"
val retrofit = "2.9.0"
val splash_screen="1.0.0"
val timber = "5.0.1"

// Use the same dependencies in multi-module.
dependencies {
    constraints {
        api("${Libs.ANDROIDX_BROWSER}:$androidx_browser")
        api("${Libs.ANDROIDX_HILT_COMPILER}:$androidx_hilt")
        api("${Libs.ANDROIDX_HILT_NAVIGATION_COMPOSE}:$androidx_hilt")
        api("${Libs.APPCOMPAT}:$appcompat")
        api("${Libs.CONSTRAINT_LAYOUT}:$constraint_layout")
        api("${Libs.CORE_KTX}:$core_ktx")
        api("${Libs.COROUTINES}:$coroutines")
        api("${Libs.COROUTINES_TEST}:$coroutines")
        api("${Libs.HILT_ANDROID}:${Versions.HILT}")
        api("${Libs.HILT_COMPILER}:${Versions.HILT}")
        api("${Libs.ACCOMPANIST_PERMISSION}:${Versions.ACCOMPANIST}")
        api("${Libs.ACCOMPANIST_SWIPE_REFRESH}:${Versions.ACCOMPANIST}")
        api("${Libs.ACCOMPANIST_SYSTEM_UI_CONTROLLER}:${Versions.ACCOMPANIST}")
        api("${Libs.JETPACK_COMPOSE_MATERIAL}:${Versions.JETPACK_COMPOSE}")
        api("${Libs.JETPACK_COMPOSE_UI_TOOLING}:${Versions.JETPACK_COMPOSE}")
        api("${Libs.JUNIT}:$junit")
        api("${Libs.LIFECYCLE_LIVE_DATA_KTX}:$lifecycle")
        api("${Libs.LIFECYCLE_VIEW_MODEL_COMPOSE}:$lifecycle")
        api("${Libs.LIFECYCLE_VIEW_MODEL_KTX}:$lifecycle")
        api("${Libs.MATERIAL}:$material")
        api("${Libs.MOCKITO}:$mockito")
        api("${Libs.MOSHI}:$moshi")
        api("${Libs.MOSHI_KOTLIN}:$moshi")
        api("${Libs.MOSHI_KOTLIN_CODEGEN}:$moshi")
        api("${Libs.NAVIGATION_COMPOSE}:${Versions.NAVIGATION}")
        api("${Libs.NAVIGATION_DYNAMIC_FEATURES_FRAGMENT}:${Versions.NAVIGATION}")
        api("${Libs.NAVIGATION_FRAGMENT_KTX}:${Versions.NAVIGATION}")
        api("${Libs.NAVIGATION_UI_KTX}:${Versions.NAVIGATION}")
        api("${Libs.OKHTTP}:$okhttp")
        api("${Libs.OKHTTP_LOGGING_INTERCEPTOR}:$okhttp")
        api("${Libs.OSS_LICENSES}:$oss_licenses")
        api("${Libs.PLAY_CORE_KTX}:$play_core_ktx")
        api("${Libs.PLAY_SERVICES_ADS}:$play_services_ads")
        api("${Libs.RECYCLER_VIEW}:$recycler_view")
        api("${Libs.RECYCLER_VIEW_SELECTION}:$recycler_view_selection")
        api("${Libs.RETROFIT}:$retrofit")
        api("${Libs.RETROFIT_CONVERTER_MOSHI}:$retrofit")
        api("${Libs.SPLASH_SCREEN}:$splash_screen")
        api("${Libs.TIMBER}:$timber")

        api("${Libs.FIREBASE_BOM}:$firebase_bom")
    }
}

publishing {
    publications {
        create<MavenPublication>("myPlatform") {
            from(components["javaPlatform"])
        }
    }
}
