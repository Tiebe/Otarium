@file:Suppress("UNUSED_VARIABLE", "OPT_IN_IS_NOT_ENABLED")

import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose") version "1.2.1"
    id("com.codingfeline.buildkonfig")
    kotlin("plugin.serialization") version "1.7.10"
    id("dev.icerock.mobile.multiplatform-resources")
}

version = "1.0"
val versionCode = 15

kotlin.sourceSets.all {
    languageSettings.optIn("kotlin.RequiresOptIn")
}

@OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
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

                implementation("nl.tiebe:magisterapi:1.1.4")


                implementation("com.github.ireward:compose-html:1.0.2")
                implementation("ca.gosyer:accompanist-pager:0.25.2")
                implementation("ca.gosyer:accompanist-pager-indicators:0.25.2")
                implementation("ca.gosyer:accompanist-swiperefresh:0.25.2")

                api("dev.icerock.moko:resources:0.20.1")
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
    namespace = "nl.tiebe.otarium"

    sourceSets.getByName("main").res.srcDir(File(buildDir, "generated/moko/androidMain/res"))
}
dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.activity:activity-compose:1.4.0")
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