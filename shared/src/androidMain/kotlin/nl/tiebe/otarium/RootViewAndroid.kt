package nl.tiebe.otarium

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.DefaultComponentContext
import nl.tiebe.otarium.ui.navigation.ProvideComponentContext

@Composable
fun RootView(rootComponentContext: DefaultComponentContext) {
    ProvideComponentContext(rootComponentContext) {
        Content()
    }
}
