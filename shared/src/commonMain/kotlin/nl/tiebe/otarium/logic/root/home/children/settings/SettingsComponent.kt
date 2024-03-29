package nl.tiebe.otarium.logic.root.home.children.settings

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import kotlinx.serialization.Serializable
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.logic.root.RootComponent
import nl.tiebe.otarium.logic.root.home.HomeComponent
import nl.tiebe.otarium.logic.root.home.children.settings.children.main.MainChildComponent
import nl.tiebe.otarium.logic.root.home.children.settings.children.ui.UIChildComponent
import nl.tiebe.otarium.logic.root.home.children.settings.children.ui.children.colors.ColorChildComponent
import nl.tiebe.otarium.magister.MagisterAccount
import nl.tiebe.otarium.utils.ui.getLocalizedString

interface SettingsComponent: HomeComponent.MenuItemComponent, BackHandlerOwner {
    val navigation: StackNavigation<Config>
    val childStack: Value<ChildStack<Config, Child>>

    val navigateRootComponent: (RootComponent.ChildScreen) -> Unit

    fun navigate(child: Config) {
        navigation.push(child)
    }

    fun back() {
        navigation.pop()
    }

    sealed class Child {
        class MainChild(val component: MainChildComponent) : Child()
        class UIChild(val component: UIChildComponent) : Child()
        class ColorChild(val component: ColorChildComponent) : Child()
    }

    @Serializable
    sealed class Config(val localizedString: String) {
        @Serializable
        data object Main : Config(getLocalizedString(MR.strings.settingsItem))

        @Serializable
        data object UI : Config(getLocalizedString(MR.strings.ui_settings))

        @Serializable
        data object Colors : Config(getLocalizedString(MR.strings.color_settings))
    }

    fun selectAccount(account: MagisterAccount)
    fun openLoginScreen()
}