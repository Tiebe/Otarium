package nl.tiebe.otarium.logic

import com.arkivanov.decompose.value.Value
import nl.tiebe.otarium.logic.home.HomeComponent
import nl.tiebe.otarium.logic.login.LoginComponent
import nl.tiebe.otarium.logic.onboarding.OnboardingComponent

interface RootComponent {
    val currentScreen: Value<ChildScreen>

    sealed class ChildScreen {
        class HomeChild(val component: HomeComponent): ChildScreen()
        class OnboardingChild(val component: OnboardingComponent): ChildScreen()
        class LoginChild(val component: LoginComponent): ChildScreen()
    }

}
