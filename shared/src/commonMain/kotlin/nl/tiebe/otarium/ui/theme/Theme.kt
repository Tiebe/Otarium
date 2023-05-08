package nl.tiebe.otarium.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.darkModeState
import nl.tiebe.otarium.ui.home.settings.items.ui.colors.colorSchemeChanged

@Composable
internal fun OtariumTheme(
    colorScheme: ColorScheme? = null,
    content: @Composable () -> Unit
) {
    var selectedColorScheme = if (Data.dynamicTheme && colorScheme != null) {
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

    LaunchedEffect(colorSchemeChanged.subscribeAsState().value) {
        selectedColorScheme = if (Data.dynamicTheme && colorScheme != null) {
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
    }


    setWindowTheme(color = selectedColorScheme.primary)

    MaterialTheme(
        colorScheme = selectedColorScheme,
        typography = Typography,
        content = content
    )
}

expect fun setWindowTheme(color: Color)