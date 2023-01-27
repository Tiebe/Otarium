pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}
rootProject.name = "Otarium"

include(":androidApp")
include(":shared")
include(":lint-rules")
