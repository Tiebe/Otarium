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

fun showAds(): Boolean {
    return settings.getBoolean("show_ads", false)
}

fun showAds(show: Boolean) {
    settings.putBoolean("show_ads", show)
}

fun ageOfConsent(): Boolean {
    return settings.getBoolean("age_of_consent", false)
}

fun ageOfConsent(above: Boolean) {
    settings.putBoolean("age_of_consent", above)
}