package nl.tiebe.otarium.logic.root.onboarding

import nl.tiebe.otarium.logic.root.RootComponent

interface OnboardingComponent {
    val navigateRootComponent: (RootComponent.ChildScreen) -> Unit

    fun exitOnboarding()

    fun notifications() {
        //TODO: setupNotifications()
    }

}