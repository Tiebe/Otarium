package nl.tiebe.otarium.store.component.home.children.settings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import nl.tiebe.otarium.store.component.home.children.settings.children.StoreUserChildComponent
import nl.tiebe.otarium.ui.home.settings.SettingsComponent
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
import nl.tiebe.otarium.ui.home.settings.items.users.UserChildComponent
import nl.tiebe.otarium.ui.root.RootComponent

class StoreSettingsComponent(
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
    override val onBack: MutableValue<() -> Unit> = MutableValue {}

    override fun registerBackHandler() {
        backHandler.register(BackCallback { onBack.value() })
    }

    override fun unregisterBackHandler() {
    }

    private fun createChild(config: SettingsComponent.Config, componentContext: ComponentContext): SettingsComponent.Child =
        when (config) {
            is SettingsComponent.Config.Main -> SettingsComponent.Child.MainChild(mainChild(componentContext))
            is SettingsComponent.Config.Ads -> SettingsComponent.Child.AdsChild(adsChild(componentContext))
            is SettingsComponent.Config.Bugs -> SettingsComponent.Child.BugsChild(bugsChild(componentContext))
            is SettingsComponent.Config.Users -> SettingsComponent.Child.UsersChild(usersChild(componentContext))
            is SettingsComponent.Config.UI -> SettingsComponent.Child.UIChild(uiChild(componentContext))
            SettingsComponent.Config.Colors -> SettingsComponent.Child.ColorChild(colorChild(componentContext))
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
        StoreUserChildComponent(
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