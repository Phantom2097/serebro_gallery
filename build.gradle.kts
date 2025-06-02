// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Android Application Lib
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

    // Android Lib for Android Modules
    alias(libs.plugins.android.library) apply false

    // Ksp
    id("com.google.devtools.ksp") version "2.0.21-1.0.28" apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}