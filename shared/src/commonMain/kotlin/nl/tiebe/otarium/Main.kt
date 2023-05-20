package nl.tiebe.otarium

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ColorScheme
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
import nl.tiebe.otarium.utils.versions.runVersionCheck

val settings: Settings = Settings()

lateinit var darkModeState: MutableState<Boolean>
val safeAreaState = mutableStateOf(PaddingValues())

//todo: back gestures ios
//todo: handmatig cijfers invoeren
//todo: soepelheid swipen overal
//todo: add settings item to show lesuitval
//todo: use tertiary color
//todo: make important emails red
//todo: add statistics tab in grades
//todo: voldoendegrens instellen in instellingen
//todo: mappen in studiewijzers

fun setup() {
    val oldVersion = settings.getInt("version", 1000)

    runVersionCheck(oldVersion)

    settings.putInt("version", BuildKonfig.versionCode)

}

@Composable
internal fun Content(componentContext: ComponentContext, colorScheme: ColorScheme? = null) {
    Content(component = DefaultRootComponent(componentContext), colorScheme = colorScheme)
}

@Composable
internal fun Content(component: RootComponent, colorScheme: ColorScheme? = null) {
    darkModeState = mutableStateOf(isSystemInDarkTheme())

    OtariumTheme(colorScheme) {
        Box(
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        ) {}
        Box(
            modifier = Modifier.fillMaxHeight(0.5f).fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {}
        Box(modifier = Modifier.padding(safeAreaState.value)) {

            val currentScreen by component.currentScreen.subscribeAsState()

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