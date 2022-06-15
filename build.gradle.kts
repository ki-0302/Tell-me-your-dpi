// Top-level build file where you can add configuration options common to all sub-projects/modules.

// For tasks
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application") version Versions.ANDROID_GRADLE_PLUGIN apply false
    id("com.android.library") version Versions.ANDROID_GRADLE_PLUGIN apply false
    id("org.jetbrains.kotlin.android") version Versions.KOTLIN apply false
    id("org.jetbrains.kotlin.kapt") version Versions.KOTLIN apply false

    // https://github.com/ben-manes/gradle-versions-plugin
    // ./gradlew dependencyUpdates
    id("com.github.ben-manes.versions") version Versions.BEN_MANES
}

buildscript {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://plugins.gradle.org/m2/") }  // for Ktlint
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.ANDROID_GRADLE_PLUGIN}")
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
    val stableKeyword = listOf("alpha", "beta", "rc", "cr", "m", "preview").any {
        version.toUpperCase().contains(it)
    }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

/**
 * ktlintとlintのレポートを1つのファイルにマージする
 */
val mergeReports by tasks.register("mergeReports") {
    mergeKtlintReports()
    mergeAndroidLintReports()
}

/**
 * UnitTestのレポートファイルをコピー
 */
val copyUnitTestReports by tasks.register("copyUnitTestReports") {
    val pathSuffix = "/build/test-results/testDebugUnitTest/"

    // ディレクトリを再作成
    val testReportsDir = File(Paths.TEST_REPORTS)
    recursiveDeleteFile(testReportsDir)
    recursiveCreateDir(testReportsDir)

    // 各モジュールのUnitTestのXMLファイルをコピーする
    File(".").listFiles()?.forEach { moduleDir ->
        if (moduleDir.isDirectory) {
            val unitTestDir = File("${moduleDir.path}$pathSuffix")
            if (unitTestDir.exists()) {
                unitTestDir.listFiles()?.filter { it.isFile }?.forEach { xmlFile ->
                    xmlFile.copyTo(File("${Paths.TEST_REPORTS}${xmlFile.name}"), true)
                }
            }
        }
    }
}

/**
 * 再帰的にディレクトリを作成。GitHub Actionsでmkdirsが動作しなかったため
 */
fun recursiveCreateDir(file: File) {
    if (file.exists()) return

    if (!file.parent.isNullOrEmpty()) {
        println(file.parent)
        recursiveCreateDir(file.parentFile)
    }
    file.mkdir()
}

/**
 * 再帰的にディレクトリ内を削除
 */
fun recursiveDeleteFile(file: File) {
    if (!file.exists()) return

    if (file.isDirectory) {
        file.listFiles()?.forEach {
            recursiveDeleteFile(it)
        }
    }

    // 上で処理した後の空のディレクトリ or ファイルを削除
    file.delete()
}


fun mergeKtlintReports() {
    val outputReportName = "ktlint-results.xml"
    val pattern = """^(<\?.*?<checkstyle .*?>\n)(.*?)(</checkstyle>)"""

    writeLintReport(Paths.KTLINT_REPORTS, outputReportName, pattern)
}

fun mergeAndroidLintReports() {
    val outputReportName = "lint-results.xml"
    val pattern = """^(<\?.*?<issues .*?>\n)(.*?)(</issues>)"""

    writeLintReport(Paths.ANDROID_LINT_REPORTS, outputReportName, pattern)
}

fun writeLintReport(path: String, outputReportName: String, pattern: String) {
    var header = ""
    var footer = ""
    var body = ""

    val outputFile = File("$path/$outputReportName")
    if (outputFile.exists()) outputFile.delete()

    File(path).list()?.sorted()?.forEach {
        val list = getLintResult("$path/$it", pattern)
        header = list[0]
        body += list[1]
        footer = list[2]
    }

    outputFile.writeText("$header$body$footer")
}

fun getLintResult(path: String, pattern: String): List<String> {
    val file = File(path)
    if (!file.exists()) {
        throw java.io.FileNotFoundException("File Not Found: $path")
    }

    val text = file.readText()

    val regex =
        Regex(pattern, RegexOption.DOT_MATCHES_ALL)

    return listOf(
        regex.find(text)?.groups?.get(1)?.value ?: "",
        regex.find(text)?.groups?.get(2)?.value ?: "",
        regex.find(text)?.groups?.get(3)?.value ?: ""
    )
}
