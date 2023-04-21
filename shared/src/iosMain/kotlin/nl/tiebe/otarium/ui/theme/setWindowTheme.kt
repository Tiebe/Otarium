package nl.tiebe.otarium.ui.theme

import androidx.compose.ui.graphics.Color
import nl.tiebe.otarium.utils.ui.IOS
import platform.UIKit.backgroundColor
import platform.UIKit.UIColor

actual fun setWindowTheme(color: Color) {
    IOS.window.backgroundColor = UIColor(red = color.red.toDouble(), green = color.green.toDouble(), blue = color.blue.toDouble(), alpha = color.alpha.toDouble())
}