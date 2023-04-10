package nl.tiebe.otarium

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.russhwolf.settings.Settings
import nl.tiebe.otarium.ui.home.HomeScreen
import nl.tiebe.otarium.ui.login.LoginScreen
import nl.tiebe.otarium.ui.onboarding.OnboardingScreen
import nl.tiebe.otarium.ui.root.DefaultRootComponent
import nl.tiebe.otarium.ui.root.RootComponent
import nl.tiebe.otarium.ui.theme.OtariumTheme
import nl.tiebe.otarium.utils.runVersionCheck

val settings: Settings = Settings()

lateinit var darkModeState: MutableState<Boolean>
val safeAreaState = mutableStateOf(PaddingValues())

fun setup() {
    val oldVersion = settings.getInt("version", 1000)

    runVersionCheck(oldVersion)

    settings.putInt("version", BuildKonfig.versionCode)

}

@Composable
internal fun Content(componentContext: ComponentContext) {
    Content(component = DefaultRootComponent(componentContext))
}

@Composable
internal fun Content(component: RootComponent) {
    setup()
    darkModeState = mutableStateOf(isSystemInDarkTheme())

    Box(modifier = Modifier.padding(safeAreaState.value)) {

        val currentScreen by component.currentScreen.subscribeAsState()

        OtariumTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                when (val screen = currentScreen) {
                    is RootComponent.ChildScreen.HomeChild -> HomeScreen(screen.component)
                    is RootComponent.ChildScreen.LoginChild -> LoginScreen(screen.component)
                    is RootComponent.ChildScreen.OnboardingChild -> OnboardingScreen(screen.component)
                }
            }
        }
    }
}

expect fun setupNotifications()

expect fun closeApp()

internal val LocalComponentContext: ProvidableCompositionLocal<ComponentContext> =
    staticCompositionLocalOf { error("Root component context was not provided") }

@Composable
internal fun ProvideComponentContext(componentContext: ComponentContext, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalComponentContext provides componentContext, content = content)
}