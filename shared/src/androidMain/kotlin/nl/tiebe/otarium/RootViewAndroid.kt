package nl.tiebe.otarium

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.DefaultComponentContext

@Composable
fun RootView(rootComponentContext: DefaultComponentContext) {
    ProvideComponentContext(rootComponentContext) {
        Content(componentContext = rootComponentContext)
    }
}
