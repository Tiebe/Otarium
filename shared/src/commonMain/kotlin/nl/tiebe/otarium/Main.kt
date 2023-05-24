package nl.tiebe.otarium

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
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

fun setup() {
    val oldVersion = settings.getInt("version", 1000)

    runVersionCheck(oldVersion)

    settings.putInt("version", BuildKonfig.versionCode)

}

@Composable
internal fun Content(componentContext: ComponentContext, lightColorScheme: ColorScheme? = null, darkColorScheme: ColorScheme? = null, padding: PaddingValues) {
    Content(component = DefaultRootComponent(componentContext), lightColorScheme, darkColorScheme, padding = padding)
}

@Composable
internal fun Content(component: RootComponent, lightColorScheme: ColorScheme? = null, darkColorScheme: ColorScheme? = null, padding: PaddingValues) {
    OtariumTheme(lightColorScheme, darkColorScheme) {
        Box(
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        ) {}
        Box() {
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