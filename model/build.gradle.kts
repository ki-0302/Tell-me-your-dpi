// The model doesn't use Android SDK, so it uses only Java.
plugins {
    id("java-library")
    kotlin("jvm")
}

dependencies {
    api(platform(project(":depconstraints")))

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}
