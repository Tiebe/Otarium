package dev.tiebe.otarium.magister

import dev.tiebe.magisterapi.api.account.LoginFlow
import dev.tiebe.magisterapi.api.account.ProfileInfoFlow
import dev.tiebe.magisterapi.response.TokenResponse

suspend fun exchangeUrl(code: String, codeVerifier: String): MagisterAccount {
    val response = LoginFlow.exchangeTokens(code, codeVerifier)

    return getAccount(response)
}

suspend fun getAccount(tokens: TokenResponse): MagisterAccount {
    val tenantUrl = ProfileInfoFlow.getTenantUrl(tokens.accessToken)
    val profileInfo = ProfileInfoFlow.getProfileInfo(tenantUrl.toString(), tokens.accessToken)
    val profileImage = ProfileInfoFlow.getProfileImage(tenantUrl.toString(), tokens.accessToken, profileInfo.person.id)

    return MagisterAccount(
        accountId = profileInfo.person.id,
        profileInfo = profileInfo,
        tenantUrl = tenantUrl.toString(),
        profileImage = profileImage
    ).also { it.tokens = tokens }
}