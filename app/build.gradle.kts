import Libs.SPLASH_SCREEN
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.android.gms.oss-licenses-plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

apply(from = "../lint.gradle.kts")

android {
    compileSdk = Versions.COMPILE_SDK

    defaultConfig {
        applicationId = "com.maho_ya.tell_me_your_dpi"
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK
        versionCode = Versions.VERSION_CODE
        versionName = Versions.VERSION_NAME

        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        // Sets Java compatibility to Java 8
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    // For Kotlin projects. Support Java 8
    kotlinOptions {
        this.jvmTarget = "1.8"
    }

    signingConfigs {
        getByName("debug") {
            val keyStoreFile = project.rootProject.file("debug.keystore")
            if (keyStoreFile.exists()) {
                storeFile = keyStoreFile
            }
        }
        register("release") {
            // Signing configuration
            // https://developer.android.com/studio/publish/app-signing#secure-shared-keystore
            val keystorePropertiesFile = project.rootProject.file("keystore.properties")

            if (keystorePropertiesFile.exists()) {

                val keystoreProperties = Properties().apply {
                    load(FileInputStream(keystorePropertiesFile))
                }

                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
            }
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            isDebuggable = true

            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            // マッピングファイルをアップロードしない設定。難読化されてCrashlyticsにあがる
            // https://firebase.google.com/docs/crashlytics/get-deobfuscated-reports?platform=android#keep_obfuscated_build_variants
            // https://firebase.google.com/docs/crashlytics/upgrade-sdk?platform=android&hl=ID#the_gradle_plugin_contains_new_flags
            firebaseCrashlytics {
                mappingFileUploadEnabled = false
            }
        }
        getByName("release") {
            // https://developer.android.com/studio/build/shrink-code?hl=ja#shrink-resources
            isMinifyEnabled = true // コード圧縮
            isShrinkResources = true // リソース圧縮
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.JETPACK_COMPOSE  // For Jetpack Compose
    }

    // Setting up to use DataBinding.
    buildFeatures {
        dataBinding = true
        viewBinding = true
        compose = true  // For Jetpack Compose
    }

    lint {
        val lintReportPath: String by project
        val lintReportFilePrefix: String by project
        val lintReportFileSuffix: String by project

        xmlReport = true
        xmlOutput = rootProject.file("${lintReportPath}${lintReportFilePrefix}${project.name}${lintReportFileSuffix}")
        abortOnError = false
        checkDependencies = false // 実行時間がかかるため、依存関係やリソースのチェックは行わない
    }
    namespace = "com.maho_ya.tell_me_your_dpi"
}

dependencies {
    api(platform(project(":depconstraints")))
    kapt(platform(project(":depconstraints")))

    implementation(project(":data"))
    implementation(project(":model"))
    implementation(project(":api"))
    implementation(project(":result"))

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(Libs.COROUTINES)
    implementation(Libs.APPCOMPAT)
    implementation(Libs.CORE_KTX)
    implementation(Libs.CONSTRAINT_LAYOUT)
    // RecyclerView
    implementation(Libs.RECYCLER_VIEW)
    implementation(Libs.RECYCLER_VIEW_SELECTION)

    // ViewModel
    implementation(Libs.LIFECYCLE_VIEW_MODEL_COMPOSE)
    implementation(Libs.LIFECYCLE_VIEW_MODEL_KTX)
    // LiveData
    implementation(Libs.LIFECYCLE_LIVE_DATA_KTX)
    // Navigation
    implementation(Libs.NAVIGATION_FRAGMENT_KTX)
    implementation(Libs.NAVIGATION_UI_KTX)
    implementation(Libs.NAVIGATION_DYNAMIC_FEATURES_FRAGMENT)
    // Material Design
    implementation(Libs.MATERIAL)
    // Dagger Hilt
    implementation(Libs.HILT_ANDROID)
    kapt(Libs.HILT_COMPILER)
    // androidx Hilt
    kapt(Libs.ANDROIDX_HILT_COMPILER)
    // Jetpack Compose
    implementation(Libs.JETPACK_COMPOSE_MATERIAL)
    implementation(Libs.JETPACK_COMPOSE_UI_TOOLING) // ComposeView, Preview, etc..
    // Play Core
    implementation(Libs.PLAY_CORE_KTX)
    // Custom Tabs
    implementation(Libs.ANDROIDX_BROWSER)
    // Firebase
    implementation(platform(Libs.FIREBASE_BOM))
    implementation(Libs.ANALYTICS)
    implementation(Libs.CRASHLYTICS)
    implementation(Libs.PERF)
    // Retrofit
    implementation(Libs.RETROFIT)
    implementation(Libs.RETROFIT_CONVERTER_MOSHI)
    // OkHttp
    implementation(Libs.OKHTTP)
    // https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor
    implementation(Libs.OKHTTP_LOGGING_INTERCEPTOR)
    // Moshi - JSON library
    implementation(Libs.MOSHI)
    implementation(Libs.MOSHI_KOTLIN) // convert kotlin class from JSON
    kapt(Libs.MOSHI_KOTLIN_CODEGEN) // annotation processor
    // Splash Screen
    implementation(Libs.SPLASH_SCREEN)

    // etc
    implementation(Libs.TIMBER)
    implementation(Libs.OSS_LICENSES)

    testImplementation(Libs.JUNIT)
    testImplementation(Libs.MOCKITO)

    androidTestImplementation("${Libs.ANDROIDX_TEST_CORE}:${Versions.ANDROIDX_TEST_CORE}")
    androidTestImplementation("${Libs.ANDROIDX_TEST_EXT}:${Versions.ANDROIDX_TEST_EXT}")
    androidTestImplementation("${Libs.ANDROIDX_TEST_ESPRESSO}:${Versions.ANDROIDX_TEST_ESPRESSO}")
    androidTestImplementation("${Libs.NAVIGATION_TEST}:${Versions.NAVIGATION}")
}
