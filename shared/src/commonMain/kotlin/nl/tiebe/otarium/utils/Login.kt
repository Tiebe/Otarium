package nl.tiebe.otarium.utils

import io.ktor.client.call.*
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import nl.tiebe.magisterapi.api.account.LoginFlow
import nl.tiebe.magisterapi.api.account.ProfileInfoFlow
import nl.tiebe.magisterapi.api.requestPOST
import nl.tiebe.magisterapi.response.TokenResponse
import nl.tiebe.otarium.EXCHANGE_URL
import nl.tiebe.otarium.magister.Tokens
import nl.tiebe.otarium.utils.server.MagisterTokenResponse

suspend fun exchangeUrl(useServer: Boolean, loginRequest: LoginRequest) {
    if (useServer) {
        val response = requestPOST(EXCHANGE_URL, loginRequest).body<LoginResponse>()

        Tokens.saveTokens(response)
    } else {
        val response = LoginFlow.exchangeTokens(loginRequest.code, loginRequest.codeVerifier)

        val tenantUrl = ProfileInfoFlow.getTenantUrl(response.accessToken)
        val accountId = ProfileInfoFlow.getProfileInfo(tenantUrl.toString(), response.accessToken).person.id

        Tokens.saveMagisterTokens(MagisterTokenResponse(accountId, tenantUrl.toString(), response))
    }

}

fun getUrl(): Pair<String, String> {
    val loginUrl = LoginFlow.createAuthURL()
    return Pair(loginUrl.url, loginUrl.codeVerifier)
}

@Serializable
data class LoginRequest(val code: String, val codeVerifier: String)
@Serializable
data class LoginResponse(val accessTokens: ServerTokens, val magisterTokens: TokenResponse, val tenantUrl: String, @Required val type: Int = 1) // Types: 1 = completion

@Serializable
data class RefreshRequest(val refreshToken: String)

@Serializable
data class ServerTokens(val accessToken: String, val refreshToken: String, val expiresAt: Long)
