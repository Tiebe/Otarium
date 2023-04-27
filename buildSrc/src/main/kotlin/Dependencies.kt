
object Version {
    const val appVersion = "3.2.0"
    const val appVersionCode = 29

    const val magister = "1.1.7"

    const val kotlin = "1.8.0"
    const val gradle = "7.4.1"
    const val appcompat = "1.6.1"
    const val material = "1.4.0"
    const val compose = "1.4.0"
    const val compose_android = "1.4.0"
    const val compose_compiler = "1.4.0"
    const val ktor = "2.3.0"
    const val decompose = "2.0.0-compose-experimental-alpha-02"
    const val moko = "0.22.0"
    const val buildkonfig = "0.13.3"
    const val gms = "4.3.15"
    const val firebase = "31.5.0"
    const val multiplatform_settings = "1.0.0"
    const val admob = "22.0.0"
    const val guava = "31.1-android"
    const val guava_coroutines = "1.7.0-RC"
}

object Compose {
    const val activity = "androidx.activity:activity-compose:1.7.1"
    const val runtime = "androidx.compose.runtime:runtime:${Version.compose_android}"
    const val ui = "androidx.compose.ui:ui:${Version.compose_android}"
    const val foundationLayout = "androidx.compose.foundation:foundation-layout:${Version.compose_android}"
    const val material = "androidx.compose.material:material:${Version.compose_android}"
}


object AndroidSdk {
    const val min = 21
    const val compile = 33
    const val target = compile
}

object iOSSdk {
    const val deploymentTarget = "11.0"
}

object Android {
    const val appcompat = "androidx.appcompat:appcompat:${Version.appcompat}"
    const val material = "com.google.android.material:material:${Version.material}"
    const val gradle = "com.android.tools.build:gradle:${Version.gradle}"
}

object Kotlin {
    const val gradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}"
    const val serialization = "org.jetbrains.kotlin:kotlin-serialization:${Version.kotlin}"
    const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:0.4.0"
}

object Ktor {
    const val client_core = "io.ktor:ktor-client-core:${Version.ktor}"
    const val client_content_negotiation = "io.ktor:ktor-client-content-negotiation:${Version.ktor}"
    const val client_logging = "io.ktor:ktor-client-logging:${Version.ktor}"
    const val serialization_json = "io.ktor:ktor-serialization-kotlinx-json:${Version.ktor}"
    const val client_logging_jvm = "io.ktor:ktor-client-logging-jvm:${Version.ktor}"
    const val client_json_jvm = "io.ktor:ktor-client-json-jvm:${Version.ktor}"
    const val client_android = "io.ktor:ktor-client-okhttp:${Version.ktor}"
    const val client_ios = "io.ktor:ktor-client-ios:${Version.ktor}"
}

object Moko {
    const val gradle = "dev.icerock.moko:resources-generator:${Version.moko}"
    const val plugin = "dev.icerock.mobile.multiplatform-resources"
    const val api = "dev.icerock.moko:resources:${Version.moko}"
    const val compose = "dev.icerock.moko:resources-compose:${Version.moko}"
}

object BuildKonfig {
    const val gradle = "com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:${Version.buildkonfig}"
    const val plugin = "com.codingfeline.buildkonfig"
}

object Firebase {
    const val classpath = "com.google.gms:google-services:${Version.gms}"
    const val bom = "com.google.firebase:firebase-bom:${Version.firebase}"
    const val analytics = "com.google.firebase:firebase-analytics-ktx"
}

object Decompose {
    const val core = "com.arkivanov.decompose:decompose:${Version.decompose}"
    const val compose = "com.arkivanov.decompose:extensions-compose-jetbrains:${Version.decompose}"
}

const val russhwolf_settings = "com.russhwolf:multiplatform-settings-no-arg:${Version.multiplatform_settings}"
const val admob = "com.google.android.gms:play-services-ads:${Version.admob}"

const val magisterAPI = "dev.tiebe:magisterapi:${Version.magister}"

object Guava {
    const val core = "com.google.guava:guava:${Version.guava}"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-guava:${Version.guava_coroutines}"
}