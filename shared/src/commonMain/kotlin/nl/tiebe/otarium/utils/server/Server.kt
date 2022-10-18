package nl.tiebe.otarium.utils.server

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import nl.tiebe.magisterapi.api.requestPOST

val client = HttpClient {
    install(WebSockets)
    install(ContentNegotiation) {
        json(json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
}

suspend fun sendFirebaseToken(accessToken: String, token: String) {
    requestPOST(DEVICE_ADD_URL, DeviceAddRequest(token), accessToken)
}

@Serializable
data class DeviceAddRequest(val firebaseToken: String)