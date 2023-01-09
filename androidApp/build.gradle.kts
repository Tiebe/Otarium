plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
}

android {
    compileSdk = AndroidSdk.compile
    namespace = "nl.tiebe.otarium.androidApp"
    defaultConfig {
        applicationId = "nl.tiebe.otarium"
        minSdk = AndroidSdk.min
        targetSdk = AndroidSdk.target
        versionCode = 17
        versionName = "2.0.1"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
        }
    }
    compileOptions {
        sourceCompatibility = org.gradle.api.JavaVersion.VERSION_11
        targetCompatibility = org.gradle.api.JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Version.compose_compiler
    }
}

dependencies {

    implementation(project(":shared"))
    implementation(Android.appcompat)
    implementation(Android.material)

    implementation(Compose.runtime)
    implementation(Compose.ui)
    implementation(Compose.foundationLayout)
    implementation(Compose.material)

    implementation(Decompose.core)
}
