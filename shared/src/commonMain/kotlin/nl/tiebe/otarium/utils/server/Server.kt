package nl.tiebe.otarium.utils.server

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import nl.tiebe.magisterapi.api.requestGET
import nl.tiebe.magisterapi.api.requestPOST
import nl.tiebe.magisterapi.response.TokenResponse
import nl.tiebe.magisterapi.response.general.year.grades.Grade
import nl.tiebe.magisterapi.response.general.year.grades.GradeInfo
import nl.tiebe.magisterapi.utils.MagisterException
import nl.tiebe.otarium.CODE_EXCHANGE_URL
import nl.tiebe.otarium.DEVICE_ADD_URL
import nl.tiebe.otarium.SERVER_GRADES_URL
import nl.tiebe.otarium.magister.Tokens
import nl.tiebe.otarium.useServer

val client = HttpClient {
    install(ContentNegotiation) {
        json(json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
}

suspend fun sendFirebaseToken(accessToken: String, token: String): Boolean {
    if (!useServer()) return false

    return try {
        requestPOST(DEVICE_ADD_URL, DeviceAddRequest(token), accessToken)
        true
    } catch (e: MagisterException) {
        false
    }
}

suspend fun getGradesFromServer(accessToken: String): List<ServerGrade>? {
    return requestGET(SERVER_GRADES_URL, hashMapOf(), accessToken).body()
}

suspend fun exchangeOTP(otp: String): LoginResponse {
    val response = requestGET(CODE_EXCHANGE_URL, hashMapOf("code" to otp)).body<LoginResponse>()

    Tokens.saveTokens(response)

    return response
}


@Serializable
data class DeviceAddRequest(val firebaseToken: String)

@Serializable
data class MagisterTokenResponse(val accountId: Int, val tenantUrl: String, val tokens: TokenResponse)

@Serializable
data class ServerGrade(
    val userUUID: String,
    val grade: Grade,
    val gradeInfo: GradeInfo
)