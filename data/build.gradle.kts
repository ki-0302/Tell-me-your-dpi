plugins {
    id("com.android.library")
    // https://kotlinlang.org/docs/reference/using-gradle.html
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("dagger.hilt.android.plugin")
}

apply(from = "../lint.gradle.kts")

android {
    compileSdk = Versions.COMPILE_SDK

    defaultConfig {
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK

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

    lint {
        val lintReportPath: String by project
        val lintReportFilePrefix: String by project
        val lintReportFileSuffix: String by project

        xmlReport = true
        xmlOutput = rootProject.file("${lintReportPath}${lintReportFilePrefix}${project.name}${lintReportFileSuffix}")
        abortOnError = false
        checkDependencies = false // 実行時間がかかるため、依存関係やリソースのチェックは行わない
    }
}

dependencies {
    api(platform(project(":depconstraints")))
    kapt(platform(project(":depconstraints")))

    implementation(project(":result"))
    implementation(project(":model"))
    implementation(project(":api"))

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(Libs.COROUTINES)
    implementation(Libs.CORE_KTX)
    // Dagger Hilt
    implementation(Libs.HILT_ANDROID)
    kapt(Libs.HILT_COMPILER)

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
    // etc
    implementation(Libs.TIMBER)

    testImplementation(Libs.JUNIT)
    testImplementation(Libs.MOCKITO)
    testImplementation(Libs.COROUTINES_TEST)
}
