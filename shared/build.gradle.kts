
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("kotlinx-serialization")
    id("org.jetbrains.compose") version Version.compose
    id("com.codingfeline.buildkonfig")
    id("dev.icerock.mobile.multiplatform-resources")
    id("com.google.gms.google-services")
}

version = "1.0"
val versionCode = 15

android {
    compileSdk = AndroidSdk.compile
    defaultConfig {
        minSdk = AndroidSdk.min
        targetSdk = AndroidSdk.target
    }

    namespace = "nl.tiebe.otarium"

    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/res", "src/commonMain/resources")
        res.srcDir(File(buildDir, "generated/moko/androidMain/res"))
    }
}

@OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
kotlin {
    android()
    ios()
    cocoapods {
        summary = "Otarium"
        homepage = "https://otarium.groosman.nl"
        ios.deploymentTarget = iOSSdk.deploymentTarget
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "test"
            isStatic = true
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Ktor.client_core)
                implementation(Ktor.client_content_negotiation)
                implementation(Ktor.client_logging)
                implementation(Ktor.serialization_json)
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.material3)
                implementation(compose.runtime)
                api(precompose)
                api(Moko.api)

                implementation(Kotlin.dateTime)
                implementation(russhwolf_settings)
                implementation(napier)

                implementation(Accompanist.pager)
                implementation(Accompanist.pager_indicators)
                implementation(Accompanist.swiperefresh)

                implementation(magisterAPI)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Ktor.client_logging_jvm)
                implementation(Ktor.client_json_jvm)
                implementation(Ktor.client_android)

                api(Moko.android)
                implementation(admob)
                implementation(project.dependencies.platform(Firebase.bom))
                implementation(Firebase.analytics)
                implementation(Firebase.messaging)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(Ktor.client_ios)
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

buildkonfig {
    packageName = "nl.tiebe.otarium"

    defaultConfigs {
        buildConfigField(INT, "versionCode", versionCode.toString())
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "nl.tiebe.otarium"
}
