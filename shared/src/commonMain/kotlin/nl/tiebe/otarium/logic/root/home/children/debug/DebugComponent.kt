package nl.tiebe.otarium.logic.root.home.children.debug

import dev.icerock.moko.resources.desc.StringDesc
import dev.tiebe.magisterapi.response.TokenResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.logic.root.RootComponent
import nl.tiebe.otarium.logic.root.home.HomeComponent
import nl.tiebe.otarium.magister.getAccount

var currentLanguage = "en"

interface DebugComponent: HomeComponent.MenuItemComponent {
    val navigateRootComponent: (RootComponent.ChildScreen) -> Unit
    val scope: CoroutineScope

    fun exportAccounts() {
        val tokenList = Data.accounts.map {
            it.tokens
        }

        //TODO: copyToClipboard(Json.encodeToString(tokenList))
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