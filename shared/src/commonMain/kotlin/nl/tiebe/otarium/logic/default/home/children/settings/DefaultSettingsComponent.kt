package nl.tiebe.otarium.logic.default.home.children.settings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.logic.default.home.children.settings.children.main.DefaultMainChildComponent
import nl.tiebe.otarium.logic.default.home.children.settings.children.ui.DefaultUIChildComponent
import nl.tiebe.otarium.logic.default.home.children.settings.children.ui.children.colors.DefaultColorChildComponent
import nl.tiebe.otarium.logic.default.login.DefaultLoginComponent
import nl.tiebe.otarium.logic.root.RootComponent
import nl.tiebe.otarium.logic.root.home.children.settings.SettingsComponent
import nl.tiebe.otarium.magister.MagisterAccount

class DefaultSettingsComponent(
    componentContext: ComponentContext,
    override val navigateRootComponent: (RootComponent.ChildScreen) -> Unit,
): SettingsComponent, ComponentContext by componentContext, BackHandlerOwner {
    override val navigation = StackNavigation<SettingsComponent.Config>()

    override val childStack: Value<ChildStack<SettingsComponent.Config, SettingsComponent.Child>> =
        childStack(
            source = navigation,
            serializer = SettingsComponent.Config.serializer(),
            initialConfiguration = SettingsComponent.Config.Main,
            handleBackButton = false, // Pop the back stack on back button press
            childFactory = ::createChild,
        )

    override fun selectAccount(account: MagisterAccount) {
        Data.selectedAccount = account
    }

    override fun openLoginScreen() {
        navigateRootComponent(
            RootComponent.ChildScreen.LoginChild(
                DefaultLoginComponent(
                    componentContext = this,
                    navigateRootComponent
                )
            )
        )
    }

    private fun createChild(config: SettingsComponent.Config, componentContext: ComponentContext): SettingsComponent.Child =
        when (config) {
            is SettingsComponent.Config.Main -> SettingsComponent.Child.MainChild(mainChild(componentContext))
            is SettingsComponent.Config.UI -> SettingsComponent.Child.UIChild(uiChild(componentContext))
            is SettingsComponent.Config.Colors -> SettingsComponent.Child.ColorChild(colorChild(componentContext))
        }

    private fun mainChild(componentContext: ComponentContext) =
        DefaultMainChildComponent(
            componentContext = componentContext,
            _navigate = ::navigate
        )

    private fun uiChild(componentContext: ComponentContext) =
        DefaultUIChildComponent(
            componentContext = componentContext,
            _navigate = ::navigate
        )

    private fun colorChild(componentContext: ComponentContext) =
        DefaultColorChildComponent(
            componentContext = componentContext,
            _navigate = ::navigate
        )

}