package nl.tiebe.otarium.logic.onboarding

import nl.tiebe.otarium.logic.RootComponent

interface OnboardingComponent {
    val rootComponent: RootComponent

    fun setOnboardingFinished()
    fun requestNotificationPermission()
}