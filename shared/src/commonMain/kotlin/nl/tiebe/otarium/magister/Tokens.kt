package nl.tiebe.otarium.magister

import io.ktor.client.call.*
import kotlinx.datetime.Clock
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.tiebe.magisterapi.api.account.LoginFlow
import nl.tiebe.magisterapi.api.requestGET
import nl.tiebe.otarium.MAGISTER_TOKENS_URL
import nl.tiebe.otarium.settings
import nl.tiebe.otarium.useServer
import nl.tiebe.otarium.utils.server.LoginResponse
import nl.tiebe.otarium.utils.server.MagisterTokenResponse

object Tokens {
    fun checkTokens(): Boolean {
        if (getPastTokens() == null) return false
        

        return true
    }

    fun saveTokens(tokens: LoginResponse) {
        settings.putString("login_tokens", Json.encodeToString(tokens))
    }


    fun getPastTokens(): LoginResponse? {
        return Json.decodeFromString(settings.getStringOrNull("login_tokens") ?: return null)
    }


    fun clearTokens() {
        settings.remove("login_tokens")
    }

    fun saveMagisterTokens(tokens: MagisterTokenResponse) {
        settings.putString("magister_tokens", Json.encodeToString(tokens))
    }

    private fun getSavedMagisterTokens(): MagisterTokenResponse? {
        return Json.decodeFromString(settings.getStringOrNull("magister_tokens") ?: return null)
    }

    suspend fun getMagisterTokens(accessToken: String?): MagisterTokenResponse? {
        val savedTokens = getSavedMagisterTokens()
        if (savedTokens?.tokens?.expiresAt?.isAfterNow == true) {
            return savedTokens
        }

        if (!useServer()) {
            val newTokens = LoginFlow.refreshToken(savedTokens?.tokens?.refreshToken ?: return null)

            return MagisterTokenResponse(savedTokens.accountId, savedTokens.tenantUrl, newTokens)
        } else {
            if (accessToken == null) {
                return null
            }

            return requestGET(
                MAGISTER_TOKENS_URL,
                hashMapOf(),
                accessToken
            ).body<MagisterTokenResponse>().also { Tokens.saveMagisterTokens(it) }
        }
    }
}

val Long.isAfterNow: Boolean
    get() = this > Clock.System.now().toEpochMilliseconds()/1000 - 20