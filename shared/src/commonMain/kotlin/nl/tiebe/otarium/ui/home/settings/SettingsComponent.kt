package nl.tiebe.otarium.ui.home.settings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.ui.home.MenuItemComponent
import nl.tiebe.otarium.ui.home.settings.items.ads.AdsChildComponent
import nl.tiebe.otarium.ui.home.settings.items.ads.DefaultAdsChildComponent
import nl.tiebe.otarium.ui.home.settings.items.bugs.BugsChildComponent
import nl.tiebe.otarium.ui.home.settings.items.bugs.DefaultBugsChildComponent
import nl.tiebe.otarium.ui.home.settings.items.main.DefaultMainChildComponent
import nl.tiebe.otarium.ui.home.settings.items.main.MainChildComponent
import nl.tiebe.otarium.ui.home.settings.items.ui.DefaultUIChildComponent
import nl.tiebe.otarium.ui.home.settings.items.ui.UIChildComponent
import nl.tiebe.otarium.ui.home.settings.items.ui.colors.ColorChildComponent
import nl.tiebe.otarium.ui.home.settings.items.ui.colors.DefaultColorChildComponent
import nl.tiebe.otarium.ui.home.settings.items.users.DefaultUserChildComponent
import nl.tiebe.otarium.ui.home.settings.items.users.UserChildComponent
import nl.tiebe.otarium.ui.root.RootComponent
import nl.tiebe.otarium.utils.ui.getLocalizedString

interface SettingsComponent: MenuItemComponent {
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
        class AdsChild(val component: AdsChildComponent) : Child()
        class BugsChild(val component: BugsChildComponent) : Child()
        class UsersChild(val component: UserChildComponent) : Child()
        class UIChild(val component: UIChildComponent) : Child()
        class ColorChild(val component: ColorChildComponent) : Child()
    }

    sealed class Config(val localizedString: String) : Parcelable {
        @Parcelize
        object Main : Config("")

        @Parcelize
        object Ads : Config(getLocalizedString(MR.strings.advertisements))

        @Parcelize
        object Bugs : Config(getLocalizedString(MR.strings.bug_report))

        @Parcelize
        object Users : Config(getLocalizedString(MR.strings.switch_user_text))

        @Parcelize
        object UI : Config(getLocalizedString(MR.strings.ui_settings))

        @Parcelize
        object Colors : Config(getLocalizedString(MR.strings.color_settings))
    }

}

class DefaultSettingsComponent(
    componentContext: ComponentContext,
    override val navigateRootComponent: (RootComponent.ChildScreen) -> Unit,
): SettingsComponent, ComponentContext by componentContext {
    override val navigation = StackNavigation<SettingsComponent.Config>()

    override val childStack: Value<ChildStack<SettingsComponent.Config, SettingsComponent.Child>> =
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
            is SettingsComponent.Config.Colors -> SettingsComponent.Child.ColorChild(colorChild(componentContext))
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

    private fun colorChild(componentContext: ComponentContext): ColorChildComponent =
        DefaultColorChildComponent(
            componentContext = componentContext,
            _navigate = ::navigate
        )
}