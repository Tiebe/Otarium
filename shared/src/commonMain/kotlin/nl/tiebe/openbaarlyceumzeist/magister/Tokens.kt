package nl.tiebe.openbaarlyceumzeist.magister

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import nl.tiebe.openbaarlyceumzeist.settings
import nl.tiebe.openbaarlyceumzeist.utils.server.LoginResponse

object Tokens {
    fun checkTokens(): Boolean {
        if (getPastTokens() == null) return false
        

        return true
    }


    fun getPastTokens(): LoginResponse? {
        return Json.decodeFromString(settings.getStringOrNull("login_tokens") ?: return null)
    }


    fun clearTokens() {
        settings.remove("login_tokens")
    }
}