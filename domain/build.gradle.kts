plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    jvmToolchain(JavaVersion.VERSION_17.toString().toInt())
}

dependencies {
    implementation(project(":core"))
    implementation(libs.coroutines.core)
    implementation(libs.koin.core)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.coroutines.test)
}
