plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
}

android {
    namespace = "nl.tiebe.otarium.wear"
    compileSdk = 34

    defaultConfig {
        applicationId = "nl.tiebe.otarium"
        minSdk = 30
        targetSdk = libs.versions.android.sdk.compile.get().toInt()
        versionCode = libs.versions.app.version.code.get().toInt()
        versionName = libs.versions.app.version.string.get()
        vectorDrawables {
            useSupportLibrary = true
        }

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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    implementation(project(":shared"))

    implementation(libs.play.services.wearable)
    implementation(platform(libs.wear.compose.bom))
    implementation(libs.compose.ui)
//    implementation(libs.wear.tooling.preview)
    implementation(libs.androidx.wear.compose.compose.material)
    implementation(libs.androidx.wear.compose.compose.material3)
    implementation(libs.compose.foundation)
    implementation(libs.compose.activity)
    implementation(libs.core.splashscreen)
    implementation(libs.androidx.wear.phone.interactions)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    implementation(libs.kotlin.datetime)

    implementation(libs.decompose.core)
    implementation(libs.decompose.compose)

    api(libs.moko.resources.core)
    implementation(libs.magister.api)
}