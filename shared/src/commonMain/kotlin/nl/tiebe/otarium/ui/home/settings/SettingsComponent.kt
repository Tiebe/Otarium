package nl.tiebe.otarium.ui.home.settings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import nl.tiebe.otarium.ui.home.MenuItemComponent
import nl.tiebe.otarium.ui.home.settings.items.ads.AdsChildComponent
import nl.tiebe.otarium.ui.home.settings.items.ads.DefaultAdsChildComponent
import nl.tiebe.otarium.ui.home.settings.items.bugs.BugsChildComponent
import nl.tiebe.otarium.ui.home.settings.items.bugs.DefaultBugsChildComponent
import nl.tiebe.otarium.ui.home.settings.items.main.DefaultMainChildComponent
import nl.tiebe.otarium.ui.home.settings.items.main.MainChildComponent
import nl.tiebe.otarium.ui.home.settings.items.ui.DefaultUIChildComponent
import nl.tiebe.otarium.ui.home.settings.items.ui.UIChildComponent
import nl.tiebe.otarium.ui.home.settings.items.users.DefaultUserChildComponent
import nl.tiebe.otarium.ui.home.settings.items.users.UserChildComponent
import nl.tiebe.otarium.ui.root.RootComponent

interface SettingsComponent: MenuItemComponent {
    val childStack: Value<ChildStack<*, Child>>

    val navigateRootComponent: (RootComponent.ChildScreen) -> Unit

    sealed class Child {
        class MainChild(val component: MainChildComponent) : Child()
        class AdsChild(val component: AdsChildComponent) : Child()
        class BugsChild(val component: BugsChildComponent) : Child()
        class UsersChild(val component: UserChildComponent) : Child()
        class UIChild(val component: UIChildComponent) : Child()
    }

    sealed class Config : Parcelable {
        @Parcelize
        object Main : Config()

        @Parcelize
        object Ads : Config()

        @Parcelize
        object Bugs : Config()

        @Parcelize
        object Users : Config()

        @Parcelize
        object UI : Config()
    }
}

class DefaultSettingsComponent(
    componentContext: ComponentContext,
    override val navigateRootComponent: (RootComponent.ChildScreen) -> Unit,
): SettingsComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<SettingsComponent.Config>()

    override val childStack: Value<ChildStack<*, SettingsComponent.Child>> =
        childStack(
            source = navigation,
            initialConfiguration = SettingsComponent.Config.Main,
            handleBackButton = true, // Pop the back stack on back button press
            childFactory = ::createChild,
        )

    private fun createChild(config: SettingsComponent.Config, componentContext: ComponentContext): SettingsComponent.Child =
        when (config) {
            is SettingsComponent.Config.Main -> SettingsComponent.Child.MainChild(mainChild(componentContext))
            is SettingsComponent.Config.Ads -> SettingsComponent.Child.AdsChild(adsChild(componentContext))
            is SettingsComponent.Config.Bugs -> SettingsComponent.Child.BugsChild(bugsChild(componentContext))
            is SettingsComponent.Config.Users -> SettingsComponent.Child.UsersChild(usersChild(componentContext))
            is SettingsComponent.Config.UI -> SettingsComponent.Child.UIChild(uiChild(componentContext))
        }

    private fun mainChild(componentContext: ComponentContext): MainChildComponent =
        DefaultMainChildComponent(
            componentContext = componentContext,
            _navigate = ::navigate
        )

    private fun adsChild(componentContext: ComponentContext): AdsChildComponent =
        DefaultAdsChildComponent(
            componentContext = componentContext,
            _navigate = ::navigate
        )

    private fun bugsChild(componentContext: ComponentContext): BugsChildComponent =
        DefaultBugsChildComponent(
            componentContext = componentContext,
            _navigate = ::navigate
        )

    private fun usersChild(componentContext: ComponentContext): UserChildComponent =
        DefaultUserChildComponent(
            componentContext = componentContext,
            _navigate = ::navigate,
            navigateRootComponent = navigateRootComponent
        )

    private fun uiChild(componentContext: ComponentContext): UIChildComponent =
        DefaultUIChildComponent(
            componentContext = componentContext,
            _navigate = ::navigate
        )

    private fun navigate(child: SettingsComponent.Config) {
        navigation.push(child)
    }

}