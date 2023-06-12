package dev.tiebe.otarium.logic.root.onboarding

import dev.tiebe.otarium.logic.root.RootComponent
import dev.tiebe.otarium.setupNotifications

interface OnboardingComponent {
    val navigateRootComponent: (RootComponent.ChildScreen) -> Unit

    fun exitOnboarding()

    fun notifications() {
        setupNotifications()
    }

}