package nl.tiebe.otarium.utils

import io.ktor.client.call.*
import nl.tiebe.magisterapi.api.account.LoginFlow
import nl.tiebe.otarium.MAGISTER_TOKENS_URL
import nl.tiebe.otarium.magister.Tokens
import nl.tiebe.otarium.useServer
import nl.tiebe.otarium.utils.server.MagisterTokenResponse

expect fun reloadTokensBackground()

expect fun refreshGradesBackground()

suspend fun refreshTokens() {
    if (useServer()) {
        Tokens.saveMagisterTokens(
            requestGET(
                MAGISTER_TOKENS_URL,
                hashMapOf(),
                Tokens.getPastTokens()?.accessTokens?.accessToken ?: return
            ).body()
        )
    } else {
        val savedTokens = Tokens.getSavedMagisterTokens()
        val newTokens = LoginFlow.refreshToken(savedTokens?.tokens?.refreshToken ?: return)

        Tokens.saveMagisterTokens(MagisterTokenResponse(savedTokens.accountId, savedTokens.tenantUrl, newTokens))
    }
}

suspend fun refreshGrades() {

}