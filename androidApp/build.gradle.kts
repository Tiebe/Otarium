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
        versionCode = Version.appVersionCode
        versionName = Version.appVersion
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
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
    composeOptions {
        kotlinCompilerExtensionVersion = Version.compose_compiler
    }
}

dependencies {

    implementation(project(":shared"))
    implementation(Android.appcompat)
    implementation(Android.material)

    implementation(Compose.activity)
    implementation(Compose.runtime)
    implementation(Compose.ui)
    implementation(Compose.foundationLayout)
    implementation(Compose.material)

    implementation(Decompose.core)

    implementation(project.dependencies.platform(Firebase.bom))
    implementation(Firebase.analytics)
}
