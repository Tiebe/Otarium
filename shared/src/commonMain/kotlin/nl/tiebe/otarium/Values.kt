package nl.tiebe.otarium

import io.ktor.http.*

val RELEASE_SERVER_URL = Url("https://otarium.groosman.nl")
@Suppress("unused")
val DEBUG_SERVER_URL = Url("http://192.168.2.37:8080")
val SERVER_URL = DEBUG_SERVER_URL

val DEVICE_URL = URLBuilder(SERVER_URL).appendPathSegments("device").build()
val DEVICE_ADD_URL = URLBuilder(DEVICE_URL).appendPathSegments("add").build()

val LOGIN_URL = URLBuilder(SERVER_URL).appendPathSegments("login").build()
val EXCHANGE_URL = URLBuilder(LOGIN_URL).appendPathSegments("exchange").build()
val CODE_EXCHANGE_URL = URLBuilder(LOGIN_URL).appendPathSegments("code").build()

val MAGISTER_TOKENS_URL = URLBuilder(SERVER_URL).appendPathSegments("magister").appendPathSegments("tokens").build()

val SERVER_GRADES_URL = URLBuilder(SERVER_URL).appendPathSegments("grades").build()


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

fun useServer(): Boolean {
    return settings.getBoolean("use_server", true)
}

fun useServer(state: Boolean) {
    settings.putBoolean("use_server", state)
}