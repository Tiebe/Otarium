package dev.tiebe.otarium.ui.home.debug

import com.arkivanov.decompose.ComponentContext
import dev.icerock.moko.resources.desc.StringDesc
import dev.tiebe.magisterapi.response.TokenResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import dev.tiebe.otarium.Data
import dev.tiebe.otarium.magister.getAccount
import dev.tiebe.otarium.ui.home.MenuItemComponent
import dev.tiebe.otarium.ui.root.RootComponent
import dev.tiebe.otarium.ui.root.componentCoroutineScope
import dev.tiebe.otarium.utils.copyToClipboard

var currentLanguage = "en"

interface DebugComponent: MenuItemComponent {
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

class DefaultDebugComponent(
    componentContext: ComponentContext,
    override val navigateRootComponent: (RootComponent.ChildScreen) -> Unit,
): DebugComponent, ComponentContext by componentContext {
    override val scope: CoroutineScope = componentCoroutineScope()


}