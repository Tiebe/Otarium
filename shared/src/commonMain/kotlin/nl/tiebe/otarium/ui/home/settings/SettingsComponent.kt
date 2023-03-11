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
import nl.tiebe.otarium.ui.home.settings.items.main.DefaultMainChildComponent
import nl.tiebe.otarium.ui.home.settings.items.main.MainChildComponent

interface SettingsComponent: MenuItemComponent {
    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {
        class MainChild(val component: MainChildComponent) : Child()
        class AdsChild(val component: AdsChildComponent) : Child()
    }

    sealed class Config : Parcelable {
        @Parcelize
        object Main : Config()

        @Parcelize
        object Ads : Config()
    }
}

class DefaultSettingsComponent(
    componentContext: ComponentContext,
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

    private fun navigate(child: SettingsComponent.Config) {
        navigation.push(child)
    }

}