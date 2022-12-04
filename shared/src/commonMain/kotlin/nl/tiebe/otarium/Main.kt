package nl.tiebe.otarium

import com.russhwolf.settings.Settings

val settings: Settings = Settings()

// TODO: Try to make jetpack compose work for ios and make our lives 100000x easier

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

