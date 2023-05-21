package nl.tiebe.otarium.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.serialization.Serializable

@Serializable
data class CustomTheme(
    val primary: Int,
    val secondary: Int,
    val tertiary: Int
) {
    fun toDarkColorScheme(): ColorScheme {
        return darkColorScheme(
            primary = Color(primary),
            secondary = Color(secondary),
            tertiary = Color(tertiary)
        )
    }

    fun toLightColorScheme(): ColorScheme {
        return lightColorScheme(
            primary = Color(primary),
            secondary = Color(secondary),
            tertiary = Color(tertiary)
        )
    }
}

val red = Color(0xFFC40808)
val green = Color(0xFF1FC43B)

val defaultLightTheme = CustomTheme(
    Color(0xFF6ACBF0).toArgb(),
    Color(0xFF1FC43B).toArgb(),
    Color(0xFFC40808).toArgb()
)

val defaultDarkTheme = CustomTheme(
    Color(0xFF0F86E4).toArgb(),
    Color(0xFF1FC43B).toArgb(),
    Color(0xFFC40808).toArgb()
)