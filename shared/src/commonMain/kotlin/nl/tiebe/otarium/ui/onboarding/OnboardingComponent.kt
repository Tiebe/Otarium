package nl.tiebe.otarium.ui.onboarding

import com.arkivanov.decompose.ComponentContext

interface OnboardingComponent {
}

class DefaultOnboardingComponent(val componentContext: ComponentContext): OnboardingComponent, ComponentContext by componentContext {


}