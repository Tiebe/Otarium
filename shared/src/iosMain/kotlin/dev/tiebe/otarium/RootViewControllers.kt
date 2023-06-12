package dev.tiebe.otarium

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.UIKit.safeAreaInsets

fun RootViewController(): UIViewController = ComposeUIViewController {
    val componentContext = DefaultComponentContext(
        lifecycle = LifecycleRegistry(),
        null, null, null
    )

    println(UIApplication.sharedApplication.keyWindow?.safeAreaInsets)

    ProvideComponentContext(componentContext) {
        setup()
        Content(componentContext = componentContext, padding = safeArea.subscribeAsState().value)
    }
}

val safeArea = MutableValue(WindowInsets(0, 0, 0, 0))

fun setSafeArea(start: Int, top: Int, end: Int, bottom: Int) {
    safeArea.value = WindowInsets(start, top, end, bottom)
}