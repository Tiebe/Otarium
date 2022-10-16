package nl.tiebe.openbaarlyceumzeist.utils.server

import io.ktor.client.call.*
import kotlinx.serialization.Serializable
import nl.tiebe.magisterapi.api.account.LoginFlow
import nl.tiebe.magisterapi.api.requestPOST
import nl.tiebe.magisterapi.response.TokenResponse

suspend fun exchangeUrl(loginRequest: LoginRequest): LoginResponse {
    val response = requestPOST(EXCHANGE_URL, loginRequest)

    return response.body()
}







@Serializable
data class LoginRequest(val code: String, val codeVerifier: String)
@Serializable
data class LoginResponse(val accessTokens: ServerTokens, val magisterTokens: TokenResponse, val tenantUrl: String)

@Serializable
data class RefreshRequest(val refreshToken: String)

@Serializable
data class ServerTokens(val accessToken: String, val refreshToken: String, val expiresAt: Long)
