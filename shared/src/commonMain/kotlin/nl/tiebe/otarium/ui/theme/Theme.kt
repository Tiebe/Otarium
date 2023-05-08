package nl.tiebe.otarium.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.darkModeState

@Composable
internal fun OtariumTheme(
    colorScheme: ColorScheme? = null,
    content: @Composable () -> Unit
) {
    val selectedColorScheme = if (Data.dynamicTheme && colorScheme != null) {
        colorScheme
    } else if (Data.customThemeEnabled) {
        when {
            darkModeState.value -> Data.customDarkTheme.toDarkColorScheme()
            else -> Data.customLightTheme.toLightColorScheme()
        }
    } else {
        when {
            darkModeState.value -> defaultDarkTheme.toDarkColorScheme()
            else -> defaultLightTheme.toLightColorScheme()
        }
    }

    setWindowTheme(color = selectedColorScheme.primary)

    MaterialTheme(
        colorScheme = selectedColorScheme,
        typography = Typography,
        content = content
    )
}

expect fun setWindowTheme(color: Color)