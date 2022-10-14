buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
        classpath("com.android.tools.build:gradle:7.2.2")
        classpath("io.realm.kotlin:gradle-plugin:1.0.1")
        classpath("com.google.gms:google-services:4.3.14")
        classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.12.0")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        mavenLocal()

        maven {
            url = uri("https://maven.pkg.github.com/Otarium/MagisterAPIKt")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}