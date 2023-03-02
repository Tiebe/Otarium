package nl.tiebe.otarium.ui.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.ui.home.DefaultHomeComponent
import nl.tiebe.otarium.ui.home.HomeComponent

interface RootComponent {
    val currentScreen: Value<ChildScreen>

    sealed class ChildScreen {
        class HomeChild(val component: HomeComponent): ChildScreen()
        //class OnboardingChild(val component: OnboardingComponent): ChildScreen()
        //class LoginChild(val component: LoginComponent): ChildScreen()
    }

}

class DefaultRootComponent(componentContext: ComponentContext): RootComponent, ComponentContext by componentContext {
    override val currentScreen: Value<RootComponent.ChildScreen> = MutableValue(getScreenOnStart())

    private fun getScreenOnStart(): RootComponent.ChildScreen {
        return if (Data.accounts.isEmpty()) {
            //RootComponent.ChildScreen.OnboardingChild(onboardingComponent(componentContext))
            RootComponent.ChildScreen.HomeChild(homeComponent(this))
        } else {
            RootComponent.ChildScreen.HomeChild(homeComponent(this))
        }
    }

    private fun homeComponent(componentContext: ComponentContext): HomeComponent =
        DefaultHomeComponent(
            componentContext = componentContext
        )
}