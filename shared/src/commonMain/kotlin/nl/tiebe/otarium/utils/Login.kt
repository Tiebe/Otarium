package nl.tiebe.otarium.utils

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import nl.tiebe.magisterapi.api.account.LoginFlow
import nl.tiebe.magisterapi.api.account.ProfileInfoFlow
import nl.tiebe.magisterapi.response.TokenResponse
import nl.tiebe.otarium.magister.Tokens
import nl.tiebe.otarium.magister.MagisterAccount

suspend fun exchangeUrl(loginRequest: LoginRequest) {
    val response = LoginFlow.exchangeTokens(loginRequest.code, loginRequest.codeVerifier)

    val tenantUrl = ProfileInfoFlow.getTenantUrl(response.accessToken)
    val accountId = ProfileInfoFlow.getProfileInfo(tenantUrl.toString(), response.accessToken).person.id

    Tokens.saveMagisterTokens(MagisterAccount(accountId, tenantUrl.toString(), response))
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
