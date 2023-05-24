package nl.tiebe.otarium

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.jetbrains.skiko.SystemTheme
import org.jetbrains.skiko.currentSystemTheme
import platform.CoreGraphics.CGFloat
import platform.UIKit.UIViewController

fun RootViewController(startPadding: CGFloat, topPadding: CGFloat, endPadding: CGFloat, bottomPadding: CGFloat): UIViewController = ComposeUIViewController {
    val componentContext = DefaultComponentContext(
        lifecycle = LifecycleRegistry(),
        null, null, null
    )


    ProvideComponentContext(componentContext) {
        setup()
        Content(componentContext = componentContext, padding = PaddingValues(startPadding.dp, topPadding.dp, endPadding.dp, bottomPadding.dp))
    }
}