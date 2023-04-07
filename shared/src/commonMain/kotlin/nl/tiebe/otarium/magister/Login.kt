package nl.tiebe.otarium.magister

import dev.tiebe.magisterapi.api.account.LoginFlow
import dev.tiebe.magisterapi.api.account.ProfileInfoFlow

suspend fun exchangeUrl(code: String, codeVerifier: String): MagisterAccount {
    val response = LoginFlow.exchangeTokens(code, codeVerifier)

    val tenantUrl = ProfileInfoFlow.getTenantUrl(response.accessToken)
    val profileInfo = ProfileInfoFlow.getProfileInfo(tenantUrl.toString(), response.accessToken)

    return MagisterAccount(
        accountId = profileInfo.person.id,
        profileInfo = profileInfo,
        tenantUrl = tenantUrl.toString(),
    ).also { it.tokens = response }
}