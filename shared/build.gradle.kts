
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("kotlinx-serialization")
    id("kotlin-parcelize")
    alias(libs.plugins.compose)
    id(libs.plugins.buildkonfig.get().pluginId)
    id(libs.plugins.mokoresources.get().pluginId)
    //id(libs.plugins.google.services.get().pluginId)
}

version = libs.versions.app.version.string.get()

android {
    compileSdk = libs.versions.android.sdk.compile.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.sdk.min.get().toInt()
    }

    namespace = "dev.tiebe.otarium"

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
    cocoapods {
        summary = "Otarium"
        homepage = "https://otarium.groosman.nl"
        ios.deploymentTarget = libs.versions.ios.target.get()
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = false
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.serialization.json)

                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.material3)
                implementation(compose.runtime)

                api(libs.moko.resources.core)
                api(libs.moko.resources.compose)

                implementation(libs.kotlin.datetime)
                implementation(libs.multiplatform.settings)
                implementation(libs.decompose.core)
                implementation(libs.decompose.compose)

                implementation(libs.magister.api)
                implementation(libs.color.math)

            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.ktor.client.logging.jvm)
                implementation(libs.ktor.client.json.jvm)
                implementation(libs.ktor.client.android)

                implementation(libs.guava.core)
                implementation(libs.guava.coroutines)

                implementation(libs.android.appcompat)
                implementation(libs.androidx.work)

            }
        }
        val iosMain by getting {
            dependencies {
                implementation(libs.ktor.client.ios)
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
    packageName = "dev.tiebe.otarium"

    defaultConfigs {
        buildConfigField(INT, "versionCode", libs.versions.app.version.code.get())
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "dev.tiebe.otarium"
}
