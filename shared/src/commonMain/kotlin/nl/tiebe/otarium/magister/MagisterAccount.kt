package nl.tiebe.otarium.magister

import kotlinx.serialization.Serializable
import dev.tiebe.magisterapi.response.TokenResponse

@Serializable
data class MagisterAccount(val accountId: Int, val tenantUrl: String, val tokens: TokenResponse)