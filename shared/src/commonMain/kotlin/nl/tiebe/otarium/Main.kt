package nl.tiebe.otarium

import com.russhwolf.settings.Settings

val settings: Settings = Settings()

class Main {

    fun setup() {
        val version = settings.getInt("version", 0)

        //REMINDER: UPDATE VERSION CODE IN BOTH THE ANDROID MODULE AND SHARED BUILDKONFIG MODULE!!

        if (version <= 14) {
            settings.remove("agenda")
        }

        settings.putInt("version", BuildKonfig.versionCode)
    }
}

