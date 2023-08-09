package nl.tiebe.otarium.logic

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import nl.tiebe.otarium.logic.home.HomeComponent
import nl.tiebe.otarium.logic.login.LoginComponent

interface RootComponent {
    val currentScreen: MutableValue<ChildScreen>

    sealed class ChildScreen {
        data class HomeChild(val component: HomeComponent): ChildScreen()
        data object OnboardingChild : ChildScreen()
        data class LoginChild(val component: LoginComponent): ChildScreen()
    }
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