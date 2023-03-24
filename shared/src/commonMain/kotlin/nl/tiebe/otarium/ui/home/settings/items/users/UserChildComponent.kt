package nl.tiebe.otarium.ui.home.settings.items.users

import com.arkivanov.decompose.ComponentContext
import nl.tiebe.otarium.ui.home.settings.SettingsComponent
import nl.tiebe.otarium.ui.login.DefaultLoginComponent
import nl.tiebe.otarium.ui.root.RootComponent

interface UserChildComponent {
    fun navigate(child: SettingsComponent.Config)

    val openLoginScreen: () -> Unit

}

class DefaultUserChildComponent(
    componentContext: ComponentContext,
    private val _navigate: (child: SettingsComponent.Config) -> Unit,
    val navigateRootComponent: (RootComponent.ChildScreen) -> Unit
) : UserChildComponent, ComponentContext by componentContext {
    override fun navigate(child: SettingsComponent.Config) {
        _navigate(child)
    }

    //todo: make this better one day
    override val openLoginScreen: () -> Unit = {
        navigateRootComponent(RootComponent.ChildScreen.LoginChild(
            DefaultLoginComponent(
            componentContext = this
        )))
    }

}