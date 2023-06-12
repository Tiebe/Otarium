package dev.tiebe.otarium.store.component.home.children.settings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import dev.tiebe.otarium.store.component.home.children.settings.children.StoreUserChildComponent
import dev.tiebe.otarium.logic.home.children.settings.SettingsComponent
import dev.tiebe.otarium.ui.home.settings.items.ads.AdsChildComponent
import dev.tiebe.otarium.ui.home.settings.items.ads.DefaultAdsChildComponent
import dev.tiebe.otarium.ui.home.settings.items.bugs.BugsChildComponent
import dev.tiebe.otarium.ui.home.settings.items.bugs.DefaultBugsChildComponent
import dev.tiebe.otarium.logic.home.children.settings.children.main.DefaultMainChildComponent
import dev.tiebe.otarium.logic.home.children.settings.children.main.MainChildComponent
import dev.tiebe.otarium.logic.home.children.settings.children.ui.DefaultUIChildComponent
import dev.tiebe.otarium.logic.home.children.settings.children.ui.UIChildComponent
import dev.tiebe.otarium.logic.home.children.settings.children.ui.children.colors.ColorChildComponent
import dev.tiebe.otarium.logic.home.children.settings.children.ui.children.colors.DefaultColorChildComponent
import dev.tiebe.otarium.logic.home.children.settings.children.users.UserChildComponent
import dev.tiebe.otarium.logic.default.RootComponent

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