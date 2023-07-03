package nl.tiebe.otarium

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.PredictiveBackGestureIcon
import com.arkivanov.decompose.extensions.compose.jetbrains.PredictiveBackGestureOverlay
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.UIKit.safeAreaInsets

@OptIn(ExperimentalDecomposeApi::class)
fun RootViewController(): UIViewController = ComposeUIViewController {
    val backDispatcher = BackDispatcher()

    val componentContext = DefaultComponentContext(
        lifecycle = LifecycleRegistry(),
        null,
        null,
        backHandler = backDispatcher
    )

    println(UIApplication.sharedApplication.keyWindow?.safeAreaInsets)

    ProvideComponentContext(componentContext) {
        setup()
        PredictiveBackGestureOverlay(
            backDispatcher = backDispatcher,
            backIcon = { progress, _ ->
                PredictiveBackGestureIcon(
                    imageVector = Icons.Default.ArrowBack,
                    progress = progress,
                )
            },
            modifier = Modifier.fillMaxSize(),
        ) {
            Content(componentContext = componentContext, padding = safeArea.subscribeAsState().value)
        }
    }
}

val safeArea = MutableValue(WindowInsets(0, 0, 0, 0))

fun setSafeArea(start: Int, top: Int, end: Int, bottom: Int) {
    safeArea.value = WindowInsets(start, top, end, bottom)
}