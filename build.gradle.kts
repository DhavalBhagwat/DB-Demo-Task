// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.android.library) apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.5.1"
}

ktlint {
    android.set(true) // Enable Android-specific rules
    outputToConsole.set(true) // Print lint results to the console
    ignoreFailures.set(false) // Fail the build on lint errors
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}
