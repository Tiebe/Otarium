package nl.tiebe.openbaarlyceumzeist.utils.server

import io.ktor.http.*
import kotlinx.serialization.Serializable
import nl.tiebe.magisterapi.api.requestPOST

/*
val SERVER_URL = Url("http://192.168.2.37:8080")

val DEVICE_URL = URLBuilder(SERVER_URL).appendPathSegments("device").build()
val DEVICE_ADD_URL = URLBuilder(DEVICE_URL).appendPathSegments("add").build()

suspend fun sendFirebaseToken(accessToken: String, token: String) {
    requestPOST(DEVICE_ADD_URL, DeviceAddRequest(token), accessToken)
}
*/

@Serializable
data class DeviceAddRequest(val firebaseToken: String)