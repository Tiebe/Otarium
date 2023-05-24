package nl.tiebe.otarium.utils.versions.v21

import dev.tiebe.magisterapi.api.account.ProfileInfoFlow
import dev.tiebe.magisterapi.response.TokenResponse
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import nl.tiebe.otarium.magister.MagisterAccount
import nl.tiebe.otarium.settings

fun migrateFromV21() {
    runBlocking {
        try {
            val currentAccount: JsonObject =
                Json.decodeFromString(settings.getStringOrNull("magister_tokens") ?: run {
                    settings.clear()
                    return@runBlocking
                })
            val accountId = currentAccount["accountId"]!!.jsonPrimitive.content.toInt()
            val tokens = Json.decodeFromString<TokenResponse>(currentAccount["tokens"]!!.jsonObject.toString())

            val profileInfo =
                ProfileInfoFlow.getProfileInfo(currentAccount["tenantUrl"]!!.jsonPrimitive.content, tokens.accessToken)

            val newAccount =
                MagisterAccount(accountId, profileInfo, currentAccount["tenantUrl"]!!.jsonPrimitive.content)

            settings.putString("agenda-$accountId", settings.getString("agenda", "[]"))
            settings.putString("tokens-${accountId}", Json.encodeToString(tokens))

            settings.remove("grades")
            settings.remove("full_grade_list")
            settings.remove("agenda")
            settings.remove("magister_tokens")

            settings.putString("accounts", Json.encodeToString(listOf(newAccount)))
        } catch (e: Exception) {
            settings.clear()
        }
    }
}