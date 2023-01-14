package nl.tiebe.otarium.magister

import kotlinx.datetime.Clock
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.tiebe.otarium.settings
import nl.tiebe.otarium.utils.refreshTokens

object Tokens {
    fun clearTokens() {
        settings.remove("magister_tokens")
    }

    fun saveMagisterTokens(tokens: MagisterAccount) {
        settings.putString("magister_tokens", Json.encodeToString(tokens))
    }

    fun getSavedMagisterTokens(): MagisterAccount? {
        return Json.decodeFromString(settings.getStringOrNull("magister_tokens") ?: return null)
    }

    suspend fun getMagisterTokens(): MagisterAccount? {
        val savedTokens = getSavedMagisterTokens()
        if (savedTokens?.tokens?.expiresAt?.isAfterNow == false) {
            return savedTokens
        }

        return refreshTokens()
    }
}

val Long.isAfterNow: Boolean
    get() = Clock.System.now().toEpochMilliseconds()/1000 + 20 > this