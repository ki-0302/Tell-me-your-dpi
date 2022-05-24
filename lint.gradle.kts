/**
 * ktlint用の設定
 * 各モジュールのbuild.gradleに次を追加する
 * apply(from = "../lint.gradle.kts")
 */

// 出力ファイルパス
val lintReportPath: String by extra("reports/lint/")
val lintReportFilePrefix: String by extra("lint-results-")
val lintReportFileSuffix: String by extra(".xml")

val ktlint by configurations.creating

dependencies {
    ktlint("com.pinterest:ktlint:0.45.1") {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }
    // ktlint(project(":custom-ktlint-ruleset")) // in case of custom ruleset
}

val outputDir = rootProject.file("./reports/ktlint/")
val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))

val ktlintCheck by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)

    description = "Check Kotlin code style."
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args = listOf(
        "--android",
        "--color",
        "--reporter=plain",
        "--reporter=checkstyle,output=" +
                rootProject.file("reports/ktlint/ktlint-results-${project.getName()}.xml"),
        "src/**/*.kt"
    )
    isIgnoreExitValue = true
}

val ktlintFormat by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)

    description = "Fix Kotlin code style deviations."
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args = listOf("-F", "src/**/*.kt")
}
