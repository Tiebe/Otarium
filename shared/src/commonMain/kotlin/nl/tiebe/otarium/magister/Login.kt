package nl.tiebe.otarium.magister

import kotlinx.serialization.Serializable
import nl.tiebe.magisterapi.api.account.LoginFlow
import nl.tiebe.magisterapi.api.account.ProfileInfoFlow

suspend fun exchangeUrl(magisterLogin: MagisterLogin) {
    val response = LoginFlow.exchangeTokens(magisterLogin.code, magisterLogin.codeVerifier)

    val tenantUrl = ProfileInfoFlow.getTenantUrl(response.accessToken)
    val accountId = ProfileInfoFlow.getProfileInfo(tenantUrl.toString(), response.accessToken).person.id

    Tokens.saveMagisterTokens(MagisterAccount(accountId, tenantUrl.toString(), response))
}

@Serializable
data class MagisterLogin(val code: String, val codeVerifier: String)