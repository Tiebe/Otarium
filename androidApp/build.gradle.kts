@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
    //id(libs.plugins.google.services.get().pluginId)
    //id(libs.plugins.firebase.crashlytics.get().pluginId)
    //id(libs.plugins.firebase.performance.get().pluginId)
}

android {
    compileSdk = libs.versions.android.sdk.compile.get().toInt()
    namespace = "dev.tiebe.otarium.androidApp"
    defaultConfig {
        applicationId = "dev.tiebe.otarium"
        minSdk = libs.versions.android.sdk.min.get().toInt()
        targetSdk = libs.versions.android.sdk.compile.get().toInt()
        versionCode = libs.versions.app.version.code.get().toInt()
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
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
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

/*    implementation(project.dependencies.platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.performance)*/
}
