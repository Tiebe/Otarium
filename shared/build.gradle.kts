
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
    id("kotlin-parcelize")
}

version = Version.appVersion

android {
    compileSdk = AndroidSdk.compile
    defaultConfig {
        minSdk = AndroidSdk.min
        targetSdk = AndroidSdk.target
    }

    namespace = "nl.tiebe.otarium"

    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/resources", "src/commonMain/resources")
        res.srcDir(File(buildDir, "generated/moko/androidMain/res"))
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

@OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
kotlin {
    android()
    ios()
    jvm("desktop") {
        jvmToolchain(11)
    }

    cocoapods {
        summary = "Otarium"
        homepage = "https://otarium.groosman.nl"
        ios.deploymentTarget = iOSSdk.deploymentTarget
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = false
        }

        pod("Google-Mobile-Ads-SDK") {
            moduleName = "GoogleMobileAds"
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
                api(Moko.api)
                api(Moko.compose)

                implementation(Kotlin.dateTime)
                implementation(russhwolf_settings)

                implementation(Decompose.core)
                implementation(Decompose.compose)

                implementation(magisterAPI)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Ktor.client_logging_jvm)
                implementation(Ktor.client_json_jvm)
                implementation(Ktor.client_android)

                implementation(admob)

                implementation(Guava.core)
                implementation(Guava.coroutines)

            }
        }
        val iosMain by getting {
            dependencies {
                implementation(Ktor.client_ios)
            }
        }
        val desktopMain by getting {
            dependencies {
                api(compose.preview)

                implementation(Ktor.client_desktop)
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
        buildConfigField(INT, "versionCode", Version.appVersionCode.toString())
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "nl.tiebe.otarium"
}
