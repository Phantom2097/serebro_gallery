plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

//    id("com.google.devtools.ksp")
}

android {
    namespace = "ru.null_checkers.form_filling_screen"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:ui"))

    // Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Dagger
//    implementation(libs.dagger)
//    ksp(libs.dagger.compiler)

    // Glide
    implementation(libs.glide)

    // Navigation include [ui, fragment]
    implementation(libs.bundles.androidx.navigation)

//    // OkHttp
//    implementation(libs.logging.interceptor)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // UI
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.fragment.ktx)

    // ViewModel
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
}