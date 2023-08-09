import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("kotlinx-serialization")
    id("kotlin-parcelize")
}

version = libs.versions.app.version.string.get()

android {
    compileSdk = libs.versions.android.sdk.compile.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.sdk.min.get().toInt()
    }

    namespace = "nl.tiebe.otarium"

    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/resources", "src/commonMain/resources")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

kotlin {
    androidTarget()
    ios()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":logic"))
                implementation(libs.magister.api)

                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.serialization.json)

                implementation(libs.kotlin.serializationjson)

                implementation(libs.kotlin.datetime)
                implementation(libs.decompose.core)

                implementation(libs.color.math)

            }
        }
        val androidMain by getting {
            dependencies {
            }
        }
        val iosMain by getting {
            dependencies {
            }
        }
    }
}

kotlin {
    targets.withType<KotlinNativeTarget> {
        binaries.all {
            freeCompilerArgs += "-Xdisable-phases=VerifyBitcode"
        }

    }
}
