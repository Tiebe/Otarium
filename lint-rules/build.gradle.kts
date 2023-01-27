plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.20")

    compileOnly("com.android.tools.lint:lint-api:26.0.0")
    testImplementation("com.android.tools.lint:lint-tests:26.0.0")
    testImplementation("junit:junit:4.13.2")

}

jar {
    manifest {
        attributes("Lint-Registry-v2", "nl.tiebe.otarium.lint.CustomLintRegistry")
    }
}