package nl.tiebe.otarium.logic.onboarding

import nl.tiebe.otarium.logic.RootComponent
import nl.tiebe.otarium.logic.root.RootComponent
import nl.tiebe.otarium.setupNotifications

interface OnboardingComponent {
    val navigateRootComponent: (RootComponent.ChildScreen) -> Unit

    fun exitOnboarding()

    fun notifications() {
        setupNotifications()
    }

}