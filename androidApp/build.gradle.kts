@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlinx-serialization")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

android {
    compileSdk = libs.versions.android.sdk.compile.get().toInt()
    namespace = "nl.tiebe.otarium.androidApp"
    defaultConfig {
        applicationId = "nl.tiebe.otarium"
        minSdk = libs.versions.android.sdk.min.get().toInt()
        targetSdk = libs.versions.android.sdk.compile.get().toInt()
        versionCode = (System.currentTimeMillis() / 1000).toInt()
        versionName = libs.versions.app.version.string.get()
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        compose = true
    }
}

dependencies {

    implementation(project(":shared"))
    implementation(libs.android.appcompat)
    implementation(libs.android.material)

    implementation(libs.compose.activity)
    implementation(libs.compose.runtime)
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.foundationlayout)
    implementation(libs.compose.material)

    implementation(libs.decompose.core)
    implementation(libs.play.services.wearable)
}
