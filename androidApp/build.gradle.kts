plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.gms.google-services")
}

android {
    compileSdk = 33
    defaultConfig {
        applicationId = "nl.tiebe.otarium"
        minSdk = 21
        targetSdk = 33
        versionCode = 15
        versionName = "1.3.1"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                "proguard-rules.pro",
                getDefaultProguardFile("proguard-android.txt"),
            )
        }
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.1"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    namespace = "nl.tiebe.otarium.android"
}

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.7.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation("androidx.work:work-runtime-ktx:2.7.1")
    implementation(platform("com.google.firebase:firebase-bom:30.3.2"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-messaging:23.1.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")

    implementation("com.google.android.gms:play-services-ads:21.3.0")

    implementation("androidx.compose.material3:material3:1.1.0-alpha02")
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.0-alpha02")
    implementation("androidx.core:core-ktx:1.9.0")
    debugImplementation("androidx.compose.ui:ui-tooling:1.4.0-alpha02")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.4.0-alpha02")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.4.0-alpha02")
    implementation("androidx.navigation:navigation-compose:2.5.3")
    implementation("androidx.compose.material:material:1.4.0-alpha02")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
}