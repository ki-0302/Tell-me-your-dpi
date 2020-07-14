import java.util.Properties
import java.io.FileInputStream
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("org.jlleitschuh.gradle.ktlint")
    id("com.google.android.gms.oss-licenses-plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(Versions.COMPILE_SDK)

    defaultConfig {
        applicationId = "com.maho_ya.tell_me_your_dpi"
        minSdkVersion(Versions.MIN_SDK)
        targetSdkVersion(Versions.TARGET_SDK)
        versionCode = Versions.versionCode
        versionName = Versions.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        // Sets Java compatibility to Java 8
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    // For Kotlin projects. Support Java 8
    kotlinOptions {
        val options = this as org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
        options.jvmTarget = "1.8"
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
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            // マッピングファイルをアップロードしない設定。難読化されてCrashlyticsにあがる
            // https://firebase.google.com/docs/crashlytics/get-deobfuscated-reports?platform=android#keep_obfuscated_build_variants
            // https://firebase.google.com/docs/crashlytics/upgrade-sdk?platform=android&hl=ID#the_gradle_plugin_contains_new_flags
            firebaseCrashlytics {
                mappingFileUploadEnabled = false
            }
        }
        getByName("release") {
            // https://developer.android.com/studio/build/shrink-code?hl=ja#shrink-resources
            isMinifyEnabled = true          // コード圧縮
            isShrinkResources = true        // リソース圧縮
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }

    // Setting up to use DataBinding.
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    lintOptions {
        xmlReport = true
    }

    // https://github.com/jlleitschuh/ktlint-gradle
    // https://github.com/JLLeitschuh/ktlint-gradle/blob/565c3e782d32fbbe91502e4b7b784ad93a050163/plugin/src/main/kotlin/org/jlleitschuh/gradle/ktlint/KtlintExtension.kt
    ktlint {
        version.set("0.37.2")
        android.set(true)
        ignoreFailures.set(true)    // When warning continued build
        // disabledRules = ["no-line-break-before-assignment"]
        reporters {
            reporter(ReporterType.CHECKSTYLE)  // for Danger
        }
    }
}

dependencies {
    api(platform(project(":depconstraints")))
    kapt(platform(project(":depconstraints")))

    implementation(project(":model"))
    implementation(project(":api"))

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(Libs.KOTLIN_STDLIB)
    implementation(Libs.APPCOMPAT)
    implementation(Libs.CORE_KTX)
    implementation(Libs.CONSTRAINT_LAYOUT)
    // RecyclerView
    implementation(Libs.RECYCLER_VIEW)
    implementation(Libs.RECYCLER_VIEW_SELECTION)

    // ViewModel
    implementation(Libs.LIFECYCLE_VIEW_MODEL_KTX)
    // LiveData
    implementation(Libs.LIFECYCLE_LIVE_DATA_KTX)
    // Navigation
    implementation(Libs.NAVIGATION_FRAGMENT_KTX)
    implementation(Libs.NAVIGATION_UI_KTX)
    implementation(Libs.NAVIGATION_DYNAMIC_FEATURES_FRAGMENT)
    //Material Design
    implementation(Libs.MATERIAL)
    // Dagger Hilt
    implementation(Libs.HILT_ANDROID)
    kapt(Libs.HILT_COMPILER)
    // androidx Hilt
    implementation(Libs.ANDROIDX_HILT_VIEW_MODEL)
    kapt(Libs.ANDROIDX_HILT_COMPILER)

    // Firebase
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
    kapt(Libs.MOSHI_KOTLIN_CODEGEN)   // annotation processor

    // AndroidBrowserHelper
    // https://github.com/GoogleChrome/android-browser-helper
    // https://developers.google.com/web/android/trusted-web-activity/integration-guide
    implementation(Libs.ANDROID_BROWSER_HELPER)
    // etc
    implementation(Libs.TIMBER)
    implementation(Libs.OSS_LICENSES)

    testImplementation(Libs.JUNIT)
    testImplementation(Libs.MOCKITO)

    androidTestImplementation(Libs.ANDROIDX_TEST_EXT)
    androidTestImplementation(Libs.ANDROIDX_TEST_ESPRESSO)
    // Testing Navigation
    androidTestImplementation(Libs.NAVIGATION_TEST)
}
