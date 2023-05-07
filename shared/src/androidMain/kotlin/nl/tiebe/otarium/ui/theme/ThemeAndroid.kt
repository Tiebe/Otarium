package nl.tiebe.otarium.ui.theme

import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import nl.tiebe.otarium.darkModeState
import nl.tiebe.otarium.utils.ui.Android

actual fun setWindowTheme(color: Color) {
    Android.window.statusBarColor = color.toArgb()

}

@Composable
actual fun getColorScheme(): ColorScheme {
    val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    return when {
        dynamicColor && darkModeState.value -> {
            dynamicDarkColorScheme(LocalContext.current)
        }
        dynamicColor && !darkModeState.value -> {
            dynamicLightColorScheme(LocalContext.current)
        }
        darkModeState.value -> DarkColorScheme
        else -> LightColorScheme
    }
}