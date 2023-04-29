package nl.tiebe.otarium

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext


@Composable
fun RootController(componentContext: ComponentContext) {
    setup()
    Content(componentContext = componentContext)
}

actual fun setupNotifications() {
}

actual fun closeApp() {
}