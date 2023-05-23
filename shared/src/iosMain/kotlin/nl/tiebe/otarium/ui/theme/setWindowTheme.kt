package nl.tiebe.otarium.ui.theme

import androidx.compose.ui.graphics.Color
import org.jetbrains.skiko.SystemTheme
import org.jetbrains.skiko.currentSystemTheme
import platform.UIKit.UIApplication
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.setStatusBarStyle

actual fun setWindowTheme(color: Color) {
    UIApplication.sharedApplication.setStatusBarStyle(if(currentSystemTheme == SystemTheme.DARK) UIStatusBarStyleLightContent else UIStatusBarStyleDarkContent)
}
