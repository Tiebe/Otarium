package nl.tiebe.otarium

import com.russhwolf.settings.Settings

val settings: Settings = Settings()

class Main {

    fun setup() {
        val version = settings.getInt("version", 0)

        if (version == 0 || version < BuildKonfig.versionCode) {
            settings.putInt("version", BuildKonfig.versionCode)
        }
    }
}

fun finishOnboarding() {
    settings.putBoolean("finished_onboarding", true)
}

fun isFinishedOnboarding(): Boolean {
    return settings.getBoolean("finished_onboarding", false)
}

fun storeBypass(): Boolean {
    return settings.getBoolean("bypass", false)
}

fun bypassStore(bypass: Boolean) {
    settings.putBoolean("bypass", bypass)
}


