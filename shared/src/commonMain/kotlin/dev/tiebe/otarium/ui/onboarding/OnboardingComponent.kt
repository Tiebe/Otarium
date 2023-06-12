package dev.tiebe.otarium.ui.onboarding

import com.arkivanov.decompose.ComponentContext
import dev.tiebe.otarium.Data
import dev.tiebe.otarium.setupNotifications
import dev.tiebe.otarium.ui.login.DefaultLoginComponent
import dev.tiebe.otarium.ui.root.RootComponent

interface OnboardingComponent {
    val navigateRootComponent: (RootComponent.ChildScreen) -> Unit

    fun exitOnboarding()

    fun notifications() {
        setupNotifications()
    }

}

class DefaultOnboardingComponent(val componentContext: ComponentContext,
                                 override val navigateRootComponent: (RootComponent.ChildScreen) -> Unit
): OnboardingComponent, ComponentContext by componentContext {
    override fun exitOnboarding() {
        Data.finishedOnboarding = true

        navigateRootComponent(RootComponent.ChildScreen.LoginChild(DefaultLoginComponent(this, navigateRootComponent)))
    }


}