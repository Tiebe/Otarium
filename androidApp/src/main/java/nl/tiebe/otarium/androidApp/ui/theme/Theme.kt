package nl.tiebe.otarium.androidApp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.logic.default.home.children.settings.children.ui.children.colors.colorSchemeChanged

@Composable
internal fun OtariumTheme(
    content: @Composable () -> Unit
) {
    val darkMode = isSystemInDarkTheme()

    val defaultColorScheme = if (darkMode) dynamicDarkColorScheme(LocalContext.current) else dynamicLightColorScheme(LocalContext.current)

    var selectedColorScheme = if (Data.dynamicTheme) {
        defaultColorScheme
    } /*else if (Data.customThemeEnabled) {
        when {
            darkMode -> Data.customDarkTheme.toDarkColorScheme()
            else -> Data.customLightTheme.toLightColorScheme()
        }

    }*/ else {
        when {
            darkMode -> defaultDarkTheme.toDarkColorScheme()
            else -> defaultLightTheme.toLightColorScheme()
        }
    }

    LaunchedEffect(colorSchemeChanged.subscribeAsState().value) {
        selectedColorScheme = if (Data.dynamicTheme) {
            defaultColorScheme
        } /*else if (Data.customThemeEnabled) {
            when {
                darkMode -> Data.customDarkTheme.toDarkColorScheme()
                else -> Data.customLightTheme.toLightColorScheme()
            }
        }*/ else {
            when {
                darkMode -> defaultDarkTheme.toDarkColorScheme()
                else -> defaultLightTheme.toLightColorScheme()
            }
        }
    }


    val systemUiController = rememberSystemUiController()
    DisposableEffect(systemUiController, darkMode) {
        systemUiController.setSystemBarsColor(
            color = selectedColorScheme.primary,
            darkIcons = !darkMode
        )

        onDispose {}
    }

    MaterialTheme(
        colorScheme = selectedColorScheme,
        typography = Typography,
        content = content
    )
}