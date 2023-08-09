package nl.tiebe.otarium.logic.home.children.debug

import dev.icerock.moko.resources.desc.StringDesc
import nl.tiebe.otarium.logic.RootComponent
import nl.tiebe.otarium.logic.home.HomeComponent

var currentLanguage = "en"

interface DebugComponent: HomeComponent.MenuItemComponent {
    /** The root component. */
    val rootComponent: RootComponent

    /**
     * Export the accounts to the clipboard.
     */
    fun exportAccounts()

    /**
     * Import the accounts from a string.
     *
     * @param accounts The accounts to import.
     */
    fun importAccounts(accounts: String)

    /**
     * Change the language.
     */
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