package nl.tiebe.otarium.logic.default.onboarding

import com.arkivanov.decompose.ComponentContext
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.logic.default.login.DefaultLoginComponent
import nl.tiebe.otarium.logic.root.RootComponent
import nl.tiebe.otarium.logic.root.onboarding.OnboardingComponent

class DefaultOnboardingComponent(val componentContext: ComponentContext,
                                 override val navigateRootComponent: (RootComponent.ChildScreen) -> Unit
): OnboardingComponent, ComponentContext by componentContext {
    override fun exitOnboarding() {
        Data.finishedOnboarding = true

        navigateRootComponent(RootComponent.ChildScreen.LoginChild(DefaultLoginComponent(this, navigateRootComponent)))
    }


}