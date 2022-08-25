package nl.tiebe.openbaarlyceumzeist.magister

import com.russhwolf.settings.contains
import io.github.aakira.napier.Napier
import kotlinx.datetime.Clock
import nl.tiebe.magisterapi.api.account.LoginFlow
import nl.tiebe.magisterapi.response.TokenResponse
import nl.tiebe.magisterapi.utils.MagisterException
import nl.tiebe.openbaarlyceumzeist.account
import nl.tiebe.openbaarlyceumzeist.settings

object Tokens {
    private suspend fun refreshTokens(tokens: TokenResponse): TokenResponse? {
        try {

            if (Clock.System.now().epochSeconds > tokens.expiresAt) {
                Napier.d(tag = "Magister", message = "Refreshing tokens")

                return saveTokens(
                    LoginFlow.refreshToken(
                        account,
                        tokens.refreshToken
                    )
                )
            }
        } catch (e: MagisterException) {
            e.message?.let { Napier.e(tag = "Magister", message = it) }
            return null
        }
        account.tokens = tokens
        Napier.d(tag = "Magister", message = "Tokens are still valid")
        return tokens
    }

    suspend fun getTokens(loginFlow: LoginFlow, code: String) =
        saveTokens(loginFlow.exchangeTokens(code))

    private fun saveTokens(tokens: TokenResponse): TokenResponse {
        Napier.d(tag = "Magister", message = "Saving tokens")
        account.tokens = tokens
        settings.putString("access_token", tokens.accessToken)
        settings.putString("refresh_token", tokens.refreshToken)
        settings.putLong("expires_at", tokens.expiresAt)
        return tokens
    }

    suspend fun getPastTokens(): TokenResponse? {
        if (settings.contains("refresh_token")) {
            val tokenResponse = refreshTokens(
                TokenResponse(
                    settings.getString(
                        "access_token"
                    ),
                    settings.getString(
                        "refresh_token"
                    ),
                    "",
                    "",
                    settings.getLong(
                        "expires_at"
                    ), ""
                ),
            )
            if (tokenResponse == null) settings.remove("refresh_token")
            else return tokenResponse
        }

        return null
    }

    fun clearTokens() {
        settings.remove("access_token")
        settings.remove("refresh_token")
        settings.remove("expires_at")
    }
}