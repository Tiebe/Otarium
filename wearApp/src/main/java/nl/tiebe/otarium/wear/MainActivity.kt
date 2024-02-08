package nl.tiebe.otarium.wear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.google.android.gms.wearable.MessageEvent
import nl.tiebe.otarium.ProvideComponentContext
import nl.tiebe.otarium.logic.default.DefaultRootComponent
import nl.tiebe.otarium.logic.root.RootComponent
import nl.tiebe.otarium.setup
import nl.tiebe.otarium.wear.theme.OtariumTheme
import nl.tiebe.otarium.wear.ui.home.HomeScreen
import nl.tiebe.otarium.wear.ui.login.LoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        val componentContext = defaultComponentContext()

        setContent {
            ProvideComponentContext(componentContext) {
                setup()
                
                WearApp(DefaultRootComponent(componentContext), componentContext)
            }
        }
    }
}

@Composable
fun WearApp(component: RootComponent, componentContext: ComponentContext) {
    OtariumTheme {
        val currentScreen by component.currentScreen.subscribeAsState()

        when (val screen = currentScreen) {
            is RootComponent.ChildScreen.HomeChild -> HomeScreen(componentContext)
            is RootComponent.ChildScreen.LoginChild -> LoginScreen(screen.component)
            is RootComponent.ChildScreen.OnboardingChild -> screen.component.exitOnboarding()
        }
    }
}