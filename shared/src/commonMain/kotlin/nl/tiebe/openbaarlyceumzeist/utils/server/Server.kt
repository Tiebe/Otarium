package nl.tiebe.openbaarlyceumzeist.utils.server

import kotlinx.serialization.Serializable
import nl.tiebe.magisterapi.api.requestPOST

suspend fun sendFirebaseToken(accessToken: String, token: String) {
    requestPOST(DEVICE_ADD_URL, DeviceAddRequest(token), accessToken)
}

@Serializable
data class DeviceAddRequest(val firebaseToken: String)