
object Version {
    const val kotlin = "1.7.20"
    const val gradle = "7.3.1"
    const val appcompat = "1.4.0"
    const val material = "1.4.0"
    const val compose = "1.3.0-beta04-dev885"
    const val compose_android = "1.2.1"
    const val compose_compiler = "1.3.2"
    const val precompose = "1.3.13"
    const val ktor = "2.1.2"
    const val java = "11"
    const val decompose = "1.0.0-beta-03-native-compose"
}

object Compose {
    const val activity = "androidx.activity:activity-compose:1.5.0"
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

const val precompose = "moe.tlaster:precompose:${Version.precompose}"

object Ktor {
    const val client_core = "io.ktor:ktor-client-core:${Version.ktor}"
    const val client_content_negotiation = "io.ktor:ktor-client-content-negotiation:${Version.ktor}"
    const val client_logging = "io.ktor:ktor-client-logging:${Version.ktor}"
    const val serialization_json = "io.ktor:ktor-serialization-kotlinx-json:${Version.ktor}"
    const val client_logging_jvm = "io.ktor:ktor-client-logging-jvm:${Version.ktor}"
    const val client_json_jvm = "io.ktor:ktor-client-json-jvm:${Version.ktor}"
    const val client_android = "io.ktor:ktor-client-android:${Version.ktor}"
    const val client_ios = "io.ktor:ktor-client-ios:${Version.ktor}"
}

object Moko {
    const val gradle = "dev.icerock.moko:resources-generator:0.20.1"
    const val plugin = "dev.icerock.mobile.multiplatform-resources"
    const val api = "dev.icerock.moko:resources:0.20.1"
    const val android = "dev.icerock.moko:resources-compose:0.20.1"
}

object BuildKonfig {
    const val gradle = "com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.12.0"
    const val plugin = "com.codingfeline.buildkonfig"
}

object Accompanist {
    const val pager = "ca.gosyer:accompanist-pager:0.25.2"
    const val pager_indicators = "ca.gosyer:accompanist-pager-indicators:0.25.2"
    const val swiperefresh = "ca.gosyer:accompanist-swiperefresh:0.25.2"
}

object Firebase {
    const val classpath = "com.google.gms:google-services:4.3.14"
    const val bom = "com.google.firebase:firebase-bom:31.1.0"
    const val analytics = "com.google.firebase:firebase-analytics-ktx"
    const val messaging = "com.google.firebase:firebase-messaging-ktx"
}

object Decompose {
    const val core = "com.arkivanov.decompose:decompose:${Version.decompose}"
    const val compose = "com.arkivanov.decompose:extensions-compose-jetbrains:${Version.decompose}"
}

const val russhwolf_settings = "com.russhwolf:multiplatform-settings-no-arg:0.9"
const val napier = "io.github.aakira:napier:2.6.1"
const val admob = "com.google.android.gms:play-services-ads:21.3.0"

const val magisterAPI = "nl.tiebe:magisterapi:1.1.5"

object Guava {
    const val core = "com.google.guava:guava:31.0.1-android"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-guava:1.6.0"
}