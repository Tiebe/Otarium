@file:Suppress("UNUSED_VARIABLE", "OPT_IN_IS_NOT_ENABLED")

import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("org.jetbrains.compose") version "1.3.0-beta03"
    id("com.codingfeline.buildkonfig")
    kotlin("plugin.serialization") version "1.7.21"
    id("dev.icerock.mobile.multiplatform-resources")
}

version = "1.0"
val versionCode = 15

kotlin.sourceSets.all {
    languageSettings.optIn("kotlin.RequiresOptIn")
}

@OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
kotlin {
    ios()
    android()
    
    sourceSets {
        val ktorVersion = "2.0.3"

        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                implementation("com.russhwolf:multiplatform-settings-no-arg:0.9")
                implementation("io.github.aakira:napier:2.6.1")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.runtime)
                implementation(compose.animation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)

                implementation("nl.tiebe:magisterapi:1.1.4")
                implementation("de.charlex.compose:html-text:1.3.1")


                implementation("com.github.ireward:compose-html:1.0.2")
                implementation("ca.gosyer:accompanist-pager:0.25.2")
                implementation("ca.gosyer:accompanist-pager-indicators:0.25.2")
                implementation("ca.gosyer:accompanist-swiperefresh:0.25.2")

                api("dev.icerock.moko:resources:0.20.1")
                api("io.github.qdsfdhvh:image-loader:1.2.3")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("dev.icerock.moko:resources-test:0.20.1")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("androidx.work:work-runtime-ktx:2.7.1")
                implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
                implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")

                api("dev.icerock.moko:resources-compose:0.20.1")
                implementation("com.google.android.gms:play-services-ads:21.3.0")
            }
        }
    }

    cocoapods {
        // Required properties
        // Specify the required Pod version here. Otherwise, the Gradle project version is used.
        version = "1.0"
        summary = "Some description for a Kotlin/Native module"
        homepage = "Link to a Kotlin/Native module homepage"

        // Optional properties
        // Configure the Pod name here instead of changing the Gradle project name
        name = "Otarium"

        framework {
            // Required properties
            // Framework name configuration. Use this property instead of deprecated 'frameworkName'
            baseName = "Otarium"

            // Optional properties
            // Dynamic framework support
            isStatic = false
            // Dependency export
            export(project(":shared"))
            transitiveExport = false // This is default.
            // Bitcode embedding
            embedBitcode(org.jetbrains.kotlin.gradle.plugin.mpp.BitcodeEmbeddingMode.BITCODE)
        }

        pod("Google-Mobile-Ads-SDK")
        pod("AFNetworking")

        // Maps custom Xcode configuration to NativeBuildType
        xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType.DEBUG
        xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType.RELEASE
    }
}

android {
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 33
    }
    namespace = "nl.tiebe.otarium"

    sourceSets.getByName("main").res.srcDir(File(buildDir, "generated/moko/androidMain/res"))

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0-alpha02"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.activity:activity-compose:1.6.1")
}

buildkonfig {
    packageName = "nl.tiebe.otarium"

    defaultConfigs {
        buildConfigField(INT, "versionCode", versionCode.toString())
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "nl.tiebe.otarium"
}

compose {
    kotlinCompilerPlugin.set("androidx.compose.compiler:compiler:1.4.0-alpha02")
}