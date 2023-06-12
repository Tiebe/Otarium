package dev.tiebe.otarium.logic.default

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import dev.tiebe.otarium.Data
import dev.tiebe.otarium.store.component.home.StoreHomeComponent
import dev.tiebe.otarium.logic.login.DefaultLoginComponent
import dev.tiebe.otarium.logic.default.onboarding.DefaultOnboardingComponent
import dev.tiebe.otarium.logic.default.onboarding.OnboardingComponent

class DefaultRootComponent(componentContext: ComponentContext): RootComponent, ComponentContext by componentContext {
    override val currentScreen: MutableValue<RootComponent.ChildScreen> = MutableValue(getScreenOnStart())

    private fun getScreenOnStart(): RootComponent.ChildScreen {
        return if (!Data.finishedOnboarding) {
            RootComponent.ChildScreen.OnboardingChild(onboardingComponent(this))
        } else if (Data.storeLoginBypass) {
            RootComponent.ChildScreen.HomeChild(storeHomeComponent(this))
        } else if (Data.accounts.isEmpty()) {
            RootComponent.ChildScreen.LoginChild(loginComponent(this))
        } else {
            RootComponent.ChildScreen.HomeChild(homeComponent(this))
        }
    }

    private fun storeHomeComponent(componentContext: ComponentContext): _root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent =
        StoreHomeComponent(
            componentContext = componentContext,
            navigateRootComponent = { screen ->
                currentScreen.value = screen
            }
        )

    private fun homeComponent(componentContext: ComponentContext): _root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent {
        println("HomeComponent")
        return _root_ide_package_.dev.tiebe.otarium.logic.default.home.DefaultHomeComponent(
            componentContext = componentContext,
            navigateRootComponent = { screen ->
                currentScreen.value = screen
            }
        )
    }

    private fun loginComponent(componentContext: ComponentContext): DefaultLoginComponent =
        DefaultLoginComponent(
            componentContext = componentContext,
            navigateRootComponent = { screen ->
                currentScreen.value = screen
            }
        )

    private fun onboardingComponent(componentContext: ComponentContext): OnboardingComponent =
        DefaultOnboardingComponent(
            componentContext = componentContext,
            navigateRootComponent = { screen ->
                currentScreen.value = screen
            }
        )
}

fun ComponentContext.componentCoroutineScope(): CoroutineScope {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    if (lifecycle.state != Lifecycle.State.DESTROYED) {
        lifecycle.doOnDestroy {
            scope.cancel()
        }
    } else {
        scope.cancel()
    }

    return scope
}