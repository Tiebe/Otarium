package nl.tiebe.otarium.logic.home.children.settings

import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import nl.tiebe.otarium.logic.RootComponent
import nl.tiebe.otarium.logic.home.HomeComponent

/** Interface for the implementation of the backend for the settings UI. */
interface SettingsComponent<Account>: HomeComponent.MenuItemComponent, BackHandlerOwner {
    /** The root component object. */
    val rootComponent: RootComponent

    /** The stack navigation. */
    val navigation: StackNavigation<Config>

    /**
     * Navigate to a submenu.
     *
     * @param child The config for that submenu child.
     */
    fun navigate(child: Config) {
        navigation.push(child)
    }

    /**
     * Navigate back to the previous menu.
     */
    fun back() {
        navigation.pop()
    }

    /**
     * The possible menus.
     */
    sealed class Config : Parcelable {
        /** The main menu */
        @Parcelize
        data object Main : Config()

        /** The UI menu. Allows changing some UI settings. */
        @Parcelize
        data object UI : Config()

        /** The colors menu. Allows changing the colors of the app. */
        @Parcelize
        data object Colors : Config()
    }

    /**
     * Set an account as the current active account.
     *
     * @param account The account to be selected.
     */
    fun selectAccount(account: Account)

    /**
     * Open the login screen to add a new account.
     */
    fun openLoginScreen()
}