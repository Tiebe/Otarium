package nl.tiebe.otarium

import android.os.Build
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.arkivanov.decompose.DefaultComponentContext
import nl.tiebe.otarium.ui.theme.DarkColorScheme
import nl.tiebe.otarium.ui.theme.LightColorScheme

@Composable
fun RootView(rootComponentContext: DefaultComponentContext) {
    ProvideComponentContext(rootComponentContext) {
        setup()

        val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

        val colorScheme = when {
            dynamicColor && darkModeState.value -> {
                dynamicDarkColorScheme(LocalContext.current)
            }
            dynamicColor && !darkModeState.value -> {
                dynamicLightColorScheme(LocalContext.current)
            }
            darkModeState.value -> DarkColorScheme
            else -> LightColorScheme
        }

        Content(componentContext = rootComponentContext, colorScheme = colorScheme)
    }
}
