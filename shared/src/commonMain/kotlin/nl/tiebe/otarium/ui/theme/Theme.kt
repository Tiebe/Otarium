package nl.tiebe.otarium.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.ui.home.settings.items.ui.colors.colorSchemeChanged

@Composable
internal fun OtariumTheme(
    lightColorScheme: ColorScheme? = null,
    darkColorScheme: ColorScheme? = null,
    darkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val defaultColorScheme = if (darkMode) darkColorScheme else lightColorScheme

    var selectedColorScheme = if (Data.dynamicTheme && defaultColorScheme != null) {
        defaultColorScheme
    } else if (Data.customThemeEnabled) {
        when {
            darkMode -> Data.customDarkTheme.toDarkColorScheme()
            else -> Data.customLightTheme.toLightColorScheme()
        }
    } else {
        when {
            darkMode -> defaultDarkTheme.toDarkColorScheme()
            else -> defaultLightTheme.toLightColorScheme()
        }
    }

    LaunchedEffect(colorSchemeChanged.subscribeAsState().value) {
        selectedColorScheme = if (Data.dynamicTheme && defaultColorScheme != null) {
            defaultColorScheme
        } else if (Data.customThemeEnabled) {
            when {
                darkMode -> Data.customDarkTheme.toDarkColorScheme()
                else -> Data.customLightTheme.toLightColorScheme()
            }
        } else {
            when {
                darkMode -> defaultDarkTheme.toDarkColorScheme()
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