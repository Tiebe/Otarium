package nl.tiebe.otarium.logic.magister

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import nl.tiebe.otarium.logic.RootComponent
import nl.tiebe.otarium.logic.data.Data
import nl.tiebe.otarium.logic.magister.home.DefaultHomeComponent

class DefaultRootComponent(componentContext: ComponentContext): RootComponent, ComponentContext by componentContext {
    override val currentScreen: MutableValue<RootComponent.ChildScreen> = MutableValue(getScreenOnStart())

    private fun getScreenOnStart(): RootComponent.ChildScreen {
        return if (!Data.finishedOnboarding) {
            RootComponent.ChildScreen.OnboardingChild
        } else if (Data.accounts.isEmpty()) {
            RootComponent.ChildScreen.LoginChild(loginComponent(this))
        } else {
            RootComponent.ChildScreen.HomeChild(homeComponent(this))
        }
    }

    private fun homeComponent(componentContext: ComponentContext): DefaultHomeComponent {
        return DefaultHomeComponent(
            componentContext = componentContext,
            navigateRootComponent = { screen ->
                currentScreen.value = screen
            }
        )
    }
}