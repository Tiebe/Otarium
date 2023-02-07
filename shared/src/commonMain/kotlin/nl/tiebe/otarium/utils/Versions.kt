package nl.tiebe.otarium.utils

import dev.tiebe.magisterapi.api.account.ProfileInfoFlow
import dev.tiebe.magisterapi.response.TokenResponse
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.magister.MagisterAccount
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

            val newAccount = MagisterAccount(
                accountId = accountId,
                profileInfo = profileInfo,
                tenantUrl = currentAccount["tenantUrl"]!!.jsonPrimitive.content,
                savedTokens = tokens
            )

            newAccount.grades = settings.getStringOrNull("grades")?.let { Json.decodeFromString(it) } ?: emptyList()
            newAccount.fullGradeList = Json.decodeFromString(settings.getString("full_grade_list", "[]"))

            newAccount.agenda = settings.getStringOrNull("agenda")?.let { Json.decodeFromString(it) } ?: emptyList()

            settings.remove("grades")
            settings.remove("full_grade_list")
            settings.remove("agenda")
            settings.remove("magister_tokens")

            Data.accounts = Data.accounts.toMutableList().apply { add(newAccount) }
        }
    }
}