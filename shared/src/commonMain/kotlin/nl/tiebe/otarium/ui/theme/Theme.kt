package nl.tiebe.otarium.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import nl.tiebe.otarium.darkModeState

val DarkColorScheme = darkColorScheme(
    primary = Blue80,
    secondary = Green80,
    tertiary = Yellow80
)

val LightColorScheme = lightColorScheme(
    primary = Blue40,
    secondary = Green40,
    tertiary = Yellow40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
internal fun OtariumTheme(
    colorScheme: ColorScheme? = null,
    content: @Composable () -> Unit
) {
    val finalColorScheme = if (colorScheme == null) {
        when {
            darkModeState.value -> DarkColorScheme
            else -> LightColorScheme
        }
    } else colorScheme

    setWindowTheme(color = finalColorScheme.primary)

    MaterialTheme(
        colorScheme = finalColorScheme,
        typography = Typography,
        content = content
    )
}

expect fun setWindowTheme(color: Color)