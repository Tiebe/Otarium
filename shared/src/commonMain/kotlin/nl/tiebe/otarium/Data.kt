package nl.tiebe.otarium

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.tiebe.otarium.magister.MagisterAccount

object Data {
    var finishedOnboarding: Boolean
        get() = settings.getBoolean("finished_onboarding", false)
        set(value) = settings.putBoolean("finished_onboarding", value)

    var storeLoginBypass: Boolean
        get() = settings.getBoolean("bypass", false)
        set(value) = settings.putBoolean("bypass", value)

    var showAds: Boolean
        get() = settings.getBoolean("show_ads", false)
        set(value) = settings.putBoolean("show_ads", value)

    var ageOfConsent: Boolean
        get() = settings.getBoolean("age_of_consent", false)
        set(value) = settings.putBoolean("age_of_consent", value)

    var accounts: List<MagisterAccount>
        get() = settings.getString("accounts", "[]").let {
            println(it)
            Json.decodeFromString(it) }
        set(value) = settings.putString("accounts", Json.encodeToString(value))

    var selectedAccount: MagisterAccount
        get() = accounts.find { it.accountId == settings.getInt("selected_account", -1) } ?: accounts.firstOrNull() ?: throw IllegalStateException("No accounts found!")
        set(value) = settings.putInt("selected_account", value.accountId)

}
