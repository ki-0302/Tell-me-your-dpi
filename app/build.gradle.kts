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
        multiDexEnabled = true
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
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.KOTLIN}")
    implementation("com.android.support:multidex:1.0.3")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("androidx.recyclerview:recyclerview-selection:1.1.0-rc01")

    val lifecycle_version = "2.2.0"
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}")
    implementation("androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:${Versions.NAVIGATION}")
    //Material Design
    implementation("com.google.android.material:material:1.1.0")
    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:${Versions.HILT}")
    kapt("com.google.dagger:hilt-android-compiler:${Versions.HILT}")
    // androidx Hilt
    val androidx_hilt_version = "1.0.0-alpha01"
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:$androidx_hilt_version")
    kapt("androidx.hilt:hilt-compiler:$androidx_hilt_version")

    // Firebase
    implementation("com.google.firebase:firebase-analytics:17.4.3")
    implementation("com.google.firebase:firebase-crashlytics:17.1.0")
    implementation("com.google.firebase:firebase-perf:19.0.7")
    // Retrofit
    val retrofit_version = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofit_version")
    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.7.2")
    // https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.7.2")
    // Moshi - JSON library
    val moshi_version = "1.9.2"
    implementation("com.squareup.moshi:moshi:$moshi_version")
    implementation("com.squareup.moshi:moshi-kotlin:$moshi_version") // convert kotlin class from JSON
    kapt("com.squareup.moshi:moshi-kotlin-codegen:$moshi_version")   // annotation processor

    // AndroidBrowserHelper
    // https://github.com/GoogleChrome/android-browser-helper
    // https://developers.google.com/web/android/trusted-web-activity/integration-guide
    implementation("com.google.androidbrowserhelper:androidbrowserhelper:1.3.1")
    // etc
    implementation("com.jakewharton.timber:timber:4.7.1")
    implementation("com.google.android.gms:play-services-oss-licenses:17.0.0")

    testImplementation("junit:junit:4.13")
    testImplementation("org.mockito:mockito-inline:3.3.1")

    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
    // Testing Navigation
    androidTestImplementation("androidx.navigation:navigation-testing:${Versions.NAVIGATION}")
}
