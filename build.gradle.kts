// Top-level build file where you can add configuration options common to all sub-projects/modules.

// For tasks
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

// https://github.com/ben-manes/gradle-versions-plugin
// .gradlew dependencyUpdates
plugins {
    id("com.github.ben-manes.versions") version Versions.BEN_MANES
}

buildscript {

    val kotlin_version by extra("1.3.72")
    repositories {
        google()
        jcenter()
        maven { url = uri("https://plugins.gradle.org/m2/") }  // for Ktlint
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.ANDROID_GRADLE_PLUGIN}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}")
        // androidx
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.NAVIGATION}")
        // lint
        classpath("org.jlleitschuh.gradle:ktlint-gradle:${Versions.KTLINT}")
        // Dagger
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Versions.HILT}")
        // Firebase
        classpath("com.google.gms:google-services:${Versions.GOOGLE_SERVICES}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:${Versions.FIREBASE_CRASHLYTICS}")
        classpath("com.google.firebase:perf-plugin:${Versions.FIREBASE_PERF}")
        // etc
        classpath("com.google.android.gms:oss-licenses-plugin:${Versions.OSS_LICENSES}")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

subprojects {
    // Setting up to use Coroutine Flow.
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.freeCompilerArgs +=
            "-Xuse-experimental=" +
                    "kotlin.Experimental," +
                    "kotlinx.coroutines.ExperimentalCoroutinesApi," +
                    "kotlinx.coroutines.InternalCoroutinesApi," +
                    "kotlinx.coroutines.FlowPreview"
    }
}

tasks.register("clean", Delete::class.java) {
    delete(project.rootProject.buildDir)
}

tasks {
    named<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask>("dependencyUpdates") {
        resolutionStrategy {
            componentSelection {
                all {
                    if (isNonStable(candidate.version) && !isNonStable(currentVersion)) {
                        reject("Release candidate")
                    }
                }
            }
        }
        checkForGradleUpdate = true
        outputFormatter = "json"
        outputDir = "build/dependencyUpdates"
        reportfileName = "report"
    }
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("alpha", "beta", "rc", "cr", "m", "preview").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}