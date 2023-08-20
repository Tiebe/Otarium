package nl.tiebe.otarium

import com.russhwolf.settings.Settings
import nl.tiebe.otarium.utils.versions.runVersionCheck

val settings: Settings = Settings()

fun setup() {
    val oldVersion = settings.getInt("version", 1000)

    runVersionCheck(oldVersion)

    settings.putInt("version", BuildKonfig.versionCode)
}
