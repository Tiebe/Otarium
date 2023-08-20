package nl.tiebe.otarium.logic.root.home.children.settings

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import nl.tiebe.otarium.logic.root.RootComponent
import nl.tiebe.otarium.logic.root.home.HomeComponent
import nl.tiebe.otarium.logic.root.home.children.settings.children.main.MainChildComponent
import nl.tiebe.otarium.logic.root.home.children.settings.children.ui.UIChildComponent
import nl.tiebe.otarium.logic.root.home.children.settings.children.ui.children.colors.ColorChildComponent
import nl.tiebe.otarium.magister.MagisterAccount

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

    sealed class Config() : Parcelable {
        @Parcelize
        object Main : Config()

        @Parcelize
        object UI : Config()

        @Parcelize
        object Colors : Config()
    }

    fun selectAccount(account: MagisterAccount)
    fun openLoginScreen()
}