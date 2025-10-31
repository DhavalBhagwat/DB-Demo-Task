plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    jvmToolchain(JavaVersion.VERSION_17.toString().toInt())
}

dependencies {
    implementation(libs.coroutines.core)
}
