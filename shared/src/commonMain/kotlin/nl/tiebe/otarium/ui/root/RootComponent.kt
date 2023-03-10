package nl.tiebe.otarium.ui.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.ui.home.DefaultHomeComponent
import nl.tiebe.otarium.ui.home.HomeComponent
import nl.tiebe.otarium.ui.login.DefaultLoginComponent

interface RootComponent {
    val currentScreen: Value<ChildScreen>

    sealed class ChildScreen {
        class HomeChild(val component: HomeComponent): ChildScreen()
        //class OnboardingChild(val component: OnboardingComponent): ChildScreen()
        class LoginChild(val component: DefaultLoginComponent): ChildScreen()
    }

}

class DefaultRootComponent(componentContext: ComponentContext): RootComponent, ComponentContext by componentContext {
    override val currentScreen: Value<RootComponent.ChildScreen> = MutableValue(getScreenOnStart())

    private fun getScreenOnStart(): RootComponent.ChildScreen {
        return if (Data.accounts.isEmpty()) {
            //RootComponent.ChildScreen.OnboardingChild(onboardingComponent(componentContext))
            RootComponent.ChildScreen.LoginChild(loginComponent(this))
        } else {
            RootComponent.ChildScreen.HomeChild(homeComponent(this))
        }
    }

    private fun homeComponent(componentContext: ComponentContext): HomeComponent =
        DefaultHomeComponent(
            componentContext = componentContext
        )

    private fun loginComponent(componentContext: ComponentContext): DefaultLoginComponent =
        DefaultLoginComponent(
            componentContext = componentContext
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