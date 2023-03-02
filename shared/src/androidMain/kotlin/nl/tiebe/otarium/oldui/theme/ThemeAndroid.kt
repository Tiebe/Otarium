package nl.tiebe.otarium.oldui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import nl.tiebe.otarium.utils.ui.Android

actual fun setWindowTheme(color: Color) {
    Android.window.statusBarColor = color.toArgb()

}