package nl.tiebe.otarium

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Text
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Application
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.jetbrains.skiko.SystemTheme
import org.jetbrains.skiko.currentSystemTheme
import platform.BackgroundTasks.BGTaskScheduler
import platform.CoreGraphics.CGFloat
import platform.UIKit.UIViewController

fun RootViewController(): UIViewController = ComposeUIViewController {
    val componentContext = DefaultComponentContext(
        lifecycle = LifecycleRegistry(),
        null, null, null
    )


    ProvideComponentContext(componentContext) {
        setup()
        Content(componentContext = componentContext)
    }
}

fun setSafeArea(start: CGFloat, top: CGFloat, end: CGFloat, bottom: CGFloat) {
    safeAreaState.value = PaddingValues(start.dp, top.dp, end.dp, bottom.dp)
}

fun setDarkMode() {
    darkModeState.value = currentSystemTheme == SystemTheme.DARK
}
