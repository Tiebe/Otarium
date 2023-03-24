package nl.tiebe.otarium.utils

import dev.tiebe.magisterapi.api.account.ProfileInfoFlow
import dev.tiebe.magisterapi.response.TokenResponse
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import nl.tiebe.otarium.settings
import nl.tiebe.otarium.setupNotifications

fun runVersionCheck(oldVersion: Int) {
    if (oldVersion <= 14) {
        settings.remove("agenda")
    }

    if (oldVersion <= 17) {
        settings.clear()
    }

    if (oldVersion <= 20) {
        setupNotifications()
    }

    if (oldVersion <= 21) {
        runBlocking {
            val currentAccount: JsonObject = Json.decodeFromString(settings.getStringOrNull("magister_tokens") ?: return@runBlocking)
            val accountId = currentAccount["accountId"]!!.jsonPrimitive.content.toInt()
            val tokens = Json.decodeFromString<TokenResponse>(currentAccount["tokens"]!!.jsonObject.toString())

            val profileInfo = ProfileInfoFlow.getProfileInfo(currentAccount["tenantUrl"]!!.jsonPrimitive.content, tokens.accessToken)

            val newAccount = mutableMapOf(
                "accountId" to accountId.toString(),
                "profileInfo" to Json.encodeToString(profileInfo),
                "tenantUrl" to currentAccount["tenantUrl"]!!.jsonPrimitive.content,
                "savedTokens" to Json.encodeToString(tokens)
            )

            settings.putString("grades-$accountId", settings.getString("grades", "[]"))
            settings.putString("full_grade_list-$accountId", settings.getString("full_grade_list", "[]"))
            settings.putString("agenda-$accountId", settings.getString("agenda", "[]"))

            settings.remove("grades")
            settings.remove("full_grade_list")
            settings.remove("agenda")
            settings.remove("magister_tokens")

            settings.putString("accounts", Json.encodeToString(listOf(newAccount)))
        }
    }

    if (oldVersion <= 22) {
        val accounts: MutableList<JsonObject> = settings.getString("accounts", "[]").let<String, List<JsonObject>> { Json.decodeFromString(it) }.toMutableList()

        for (account in accounts) {
            settings.putString("tokens-${account["accountId"]!!.jsonPrimitive.content}", account["savedTokens"]!!.jsonObject.toString())

            accounts[accounts.indexOf(account)] = Json.encodeToJsonElement(account.toMutableMap().apply { remove("savedTokens") }).jsonObject
        }

        settings.putString("accounts", Json.encodeToString(accounts))
    }
}