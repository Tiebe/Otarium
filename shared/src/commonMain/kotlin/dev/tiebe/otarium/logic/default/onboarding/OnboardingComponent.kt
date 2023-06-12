package dev.tiebe.otarium.logic.default.onboarding

import com.arkivanov.decompose.ComponentContext
import dev.tiebe.otarium.Data
import dev.tiebe.otarium.setupNotifications
import dev.tiebe.otarium.logic.login.DefaultLoginComponent
import dev.tiebe.otarium.logic.default.RootComponent
import dev.tiebe.otarium.logic.default.login.DefaultLoginComponent
import dev.tiebe.otarium.logic.root.RootComponent
import dev.tiebe.otarium.logic.root.onboarding.OnboardingComponent

class DefaultOnboardingComponent(val componentContext: ComponentContext,
                                 override val navigateRootComponent: (RootComponent.ChildScreen) -> Unit
): OnboardingComponent, ComponentContext by componentContext {
    override fun exitOnboarding() {
        Data.finishedOnboarding = true

        navigateRootComponent(RootComponent.ChildScreen.LoginChild(DefaultLoginComponent(this, navigateRootComponent)))
    }


}