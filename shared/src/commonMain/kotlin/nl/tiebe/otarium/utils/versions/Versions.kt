package nl.tiebe.otarium.utils.versions

import dev.tiebe.magisterapi.api.account.LoginFlow
import dev.tiebe.magisterapi.api.account.ProfileInfoFlow
import dev.tiebe.magisterapi.response.TokenResponse
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.magister.isAfterNow
import nl.tiebe.otarium.magister.refreshMessages
import nl.tiebe.otarium.settings
import nl.tiebe.otarium.setupNotifications
import nl.tiebe.otarium.utils.versions.v21.migrateFromV21

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
        migrateFromV21()
    }

    if (oldVersion <= 33) {
        try {
            Data.selectedAccount.accountId
        } catch (_: RuntimeException) {
            settings.clear()
        }
    }

    if (oldVersion <= 34) {
        runBlocking {
            val accounts = settings.getString("accounts", "[]")
            val accountsJsonArray = Json.parseToJsonElement(accounts).jsonArray

            val modifiedAccounts = accountsJsonArray.map { account ->
                val mutableAccount = account.jsonObject.toMutableMap()
                val accountId = mutableAccount["accountId"]!!.jsonPrimitive.int

                val tokens = run {
                    val savedTokens: TokenResponse =
                        settings.getStringOrNull("tokens-$accountId")?.let { Json.decodeFromString(it) }
                            ?: throw IllegalStateException("No tokens found!")

                    if (!savedTokens.expiresAt.isAfterNow) {
                        savedTokens
                    } else LoginFlow.refreshToken(savedTokens.refreshToken)
                }

                val profileImage =
                    ProfileInfoFlow.getProfileImage(
                        mutableAccount["tenantUrl"]!!.jsonPrimitive.content,
                        tokens.accessToken,
                        accountId
                    )


                settings.putString("tokens-$accountId", Json.encodeToString(tokens))
                mutableAccount["profileImage"] = Json.encodeToJsonElement(profileImage)
                JsonObject(mutableAccount)
            }

            settings.putString("accounts", Json.encodeToString(modifiedAccounts))
        }
    }

    if (oldVersion <= 35) {
        runBlocking {
            for (account in Data.accounts) {
                account.refreshMessages { _, _ -> }
            }
        }
    }
}