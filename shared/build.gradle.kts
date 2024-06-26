
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("kotlinx-serialization")
    alias(libs.plugins.compose)
    id(libs.plugins.buildkonfig.get().pluginId)
    id(libs.plugins.mokoresources.get().pluginId)
    alias(libs.plugins.compose.compiler)
    //id(libs.plugins.google.services.get().pluginId)
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
        res.srcDir(File(layout.buildDirectory.asFile.get(), "generated/moko/androidMain/res"))
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

}

@OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
kotlin {
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
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
        commonMain {
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

                implementation(libs.skiko)
                implementation(libs.richeditor)

                implementation("androidx.compose.material:material-icons-extended:1.6.5")
            }
        }
        androidMain {
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
        iosMain {
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
    packageName = "nl.tiebe.otarium"

    defaultConfigs {
        buildConfigField(INT, "versionCode", (System.currentTimeMillis() / 1000).toInt().toString())
    }
}

multiplatformResources {
    resourcesPackage.set("nl.tiebe.otarium")
}
