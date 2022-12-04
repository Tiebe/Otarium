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
import nl.tiebe.otarium.DEVICE_ADD_URL
import nl.tiebe.otarium.MAGISTER_TOKENS_URL
import nl.tiebe.otarium.SERVER_GRADES_URL
import nl.tiebe.otarium.magister.Tokens
import nl.tiebe.otarium.magister.isAfterNow

val client = HttpClient {
    install(ContentNegotiation) {
        json(json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
}

suspend fun sendFirebaseToken(accessToken: String, token: String): Boolean {
    return try {
        requestPOST(DEVICE_ADD_URL, DeviceAddRequest(token), accessToken)
        true
    } catch (e: MagisterException) {
        false
    }
}

suspend fun getMagisterTokens(accessToken: String?): MagisterTokenResponse? {
    if (Tokens.getSavedMagisterTokens()?.tokens?.expiresAt?.isAfterNow == true) {
        Tokens.getSavedMagisterTokens()?.let { return it }
    }

    if (accessToken == null) {
        return null
    }

    return requestGET(MAGISTER_TOKENS_URL, hashMapOf(), accessToken).body<MagisterTokenResponse>().also { Tokens.saveMagisterTokens(it) }
}

suspend fun getGradesFromServer(accessToken: String): List<ServerGrade>? {
    return requestGET(SERVER_GRADES_URL, hashMapOf(), accessToken).body()
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