package nl.tiebe.openbaarlyceumzeist.utils.server

import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import nl.tiebe.magisterapi.api.account.LoginFlow
import nl.tiebe.magisterapi.response.TokenResponse
import nl.tiebe.openbaarlyceumzeist.magister.Tokens

suspend fun exchangeUrl(loginRequest: LoginRequest): LoginResponse {
    var response: LoginResponse? = null

    client.webSocket(host = EXCHANGE_URL.host, port = EXCHANGE_URL.port, path = EXCHANGE_URL.encodedPath) {
        send(Json.encodeToString(loginRequest))

        incoming.consumeEach { frame ->
            println("Received frame: $frame")
            if (frame is Frame.Text) {
                val json = Json.parseToJsonElement(frame.readText()).jsonObject
                if (json["type"].toString().toInt() == 1) {
                    response = Json.decodeFromString<LoginResponse>(frame.readText())
                }
            } else if (frame is Frame.Close) {
                if (frame.readReason()?.knownReason != CloseReason.Codes.NORMAL) {
                    println("Error: ${frame.readReason()?.message}")
                    throw Exception("Received: ${frame.readReason()?.message}")
                }
            }
        }
    }

    while (response == null) {
        // Wait for session to be initialized
    }

    Tokens.saveTokens(response!!)
    return response!!
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
