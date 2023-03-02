package nl.tiebe.otarium

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.DefaultComponentContext
import nl.tiebe.otarium.oldui.navigation.ProvideComponentContext

@Composable
fun RootView(rootComponentContext: DefaultComponentContext) {
    ProvideComponentContext(rootComponentContext) {
        Content(componentContext = rootComponentContext)
    }
}
