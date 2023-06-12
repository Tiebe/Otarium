package dev.tiebe.otarium.logic.root

import com.arkivanov.decompose.value.Value
import dev.tiebe.otarium.logic.default.onboarding.OnboardingComponent
import dev.tiebe.otarium.logic.root.home.HomeComponent
import dev.tiebe.otarium.logic.root.login.LoginComponent

interface RootComponent {
    val currentScreen: Value<ChildScreen>

    sealed class ChildScreen {
        class HomeChild(val component: HomeComponent): ChildScreen()
        class OnboardingChild(val component: OnboardingComponent): ChildScreen()
        class LoginChild(val component: LoginComponent): ChildScreen()
    }

}
