package nl.tiebe.openbaarlyceumzeist.magister

import nl.tiebe.openbaarlyceumzeist.settings

object Tokens {
    fun checkTokens(): Boolean {
        if (getPastTokens()[0].isBlank()) return false
        

        return true
    }


    fun getPastTokens(): List<String> {
        val refreshToken = settings.getStringOrNull("refresh_token")

        if (refreshToken.isNullOrBlank()) return listOf()
        return listOf(refreshToken)
    }


    fun clearTokens() {
        settings.remove("access_token")
        settings.remove("refresh_token")
        settings.remove("expires_at")
    }
}