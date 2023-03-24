package nl.tiebe.otarium.magister

import dev.tiebe.magisterapi.api.account.LoginFlow
import dev.tiebe.magisterapi.api.account.ProfileInfoFlow
import kotlinx.serialization.Serializable

suspend fun exchangeUrl(magisterLogin: MagisterLogin): MagisterAccount {
    val response = LoginFlow.exchangeTokens(magisterLogin.code, magisterLogin.codeVerifier)

    val tenantUrl = ProfileInfoFlow.getTenantUrl(response.accessToken)
    val profileInfo = ProfileInfoFlow.getProfileInfo(tenantUrl.toString(), response.accessToken)

    return MagisterAccount(
        accountId = profileInfo.person.id,
        profileInfo = profileInfo,
        tenantUrl = tenantUrl.toString(),
    ).also { it.tokens = response }
}

@Serializable
data class MagisterLogin(val code: String, val codeVerifier: String)