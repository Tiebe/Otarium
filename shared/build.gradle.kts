@file:Suppress("UNUSED_VARIABLE")

import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.codingfeline.buildkonfig")
    kotlin("plugin.serialization") version "1.7.10"
}

version = "1.0"
val versionCode = 1

kotlin {
    android()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }
    
    sourceSets {
        val ktorVersion = "2.0.3"

        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("io.ktor:ktor-client-websockets:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                implementation("com.russhwolf:multiplatform-settings-no-arg:0.9")
                implementation("io.github.aakira:napier:2.6.1")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

                implementation("nl.tiebe:magisterapi:1.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("androidx.work:work-runtime-ktx:2.7.1")
                implementation("androidx.navigation:navigation-fragment-ktx:2.5.2")
                implementation("androidx.navigation:navigation-ui-ktx:2.5.2")
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
            }
        }
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 33
    }
    namespace = "nl.tiebe.openbaarlyceumzeist"
}

buildkonfig {
    packageName = "nl.tiebe.openbaarlyceumzeist"

    defaultConfigs {
        buildConfigField(INT, "versionCode", versionCode.toString())
    }
}