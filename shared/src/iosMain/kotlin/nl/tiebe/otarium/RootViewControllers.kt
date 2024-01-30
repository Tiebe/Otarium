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
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import platform.UIKit.UIApplication
import platform.UIKit.UIColor
import platform.UIKit.UIViewController

@OptIn(ExperimentalDecomposeApi::class)
fun RootViewController(): UIViewController = ComposeUIViewController {
    val backDispatcher = BackDispatcher()

    val componentContext = DefaultComponentContext(
        lifecycle = LifecycleRegistry(),
        null,
        null,
        backHandler = backDispatcher
    )

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
            Content(componentContext = componentContext)
        }
    }
}.apply {
    view.backgroundColor = UIColor.clearColor
    view.opaque = true
    view.setClipsToBounds(true)
}