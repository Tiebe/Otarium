import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("kotlinx-serialization")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id(libs.plugins.mokoresources.get().pluginId)
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
                implementation(libs.kotlin.serializationjson)

                implementation(libs.kotlin.datetime)
                implementation(libs.decompose.core)

                implementation(libs.color.math)

                implementation(libs.kotlin.coroutines)

                api(libs.moko.resources.core)
                implementation(libs.multiplatform.settings)

            }
        }
        val androidMain by getting {
            dependencies {
                dependsOn(commonMain)
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

multiplatformResources {
    multiplatformResourcesPackage = "nl.tiebe.otarium"
}