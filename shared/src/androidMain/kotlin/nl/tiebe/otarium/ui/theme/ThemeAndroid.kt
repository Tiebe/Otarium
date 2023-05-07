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