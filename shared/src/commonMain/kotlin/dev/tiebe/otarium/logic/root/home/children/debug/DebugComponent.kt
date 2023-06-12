package dev.tiebe.otarium.logic.root.home.children.debug

import dev.icerock.moko.resources.desc.StringDesc
import dev.tiebe.magisterapi.response.TokenResponse
import dev.tiebe.otarium.Data
import dev.tiebe.otarium.logic.default.RootComponent
import dev.tiebe.otarium.logic.root.home.HomeComponent
import dev.tiebe.otarium.magister.getAccount
import dev.tiebe.otarium.utils.copyToClipboard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

var currentLanguage = "en"

interface DebugComponent: HomeComponent.MenuItemComponent {
    val navigateRootComponent: (RootComponent.ChildScreen) -> Unit
    val scope: CoroutineScope

    fun exportAccounts() {
        val tokenList = Data.accounts.map {
            it.tokens
        }

        copyToClipboard(Json.encodeToString(tokenList))
    }

    fun importAccounts(accounts: String) {
        scope.launch {
            val tokenList: List<TokenResponse> = Json.decodeFromString(accounts)

            val accountList = tokenList.map {
                getAccount(it)
            }

            for (account in accountList) {
                if (Data.accounts.find { acc -> acc.profileInfo.person.id == account.profileInfo.person.id } == null) {
                    Data.accounts =
                        Data.accounts.toMutableList().apply { add(account) }
                }
            }
        }
    }

    fun changeLanguage() {
        if (currentLanguage == "en") {
            StringDesc.localeType = StringDesc.LocaleType.Custom("nl")
            currentLanguage = "nl"
        } else {
            StringDesc.localeType = StringDesc.LocaleType.Custom("en")
            currentLanguage = "en"
        }
    }
}