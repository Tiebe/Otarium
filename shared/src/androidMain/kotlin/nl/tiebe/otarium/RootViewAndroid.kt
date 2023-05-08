package nl.tiebe.otarium

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.DefaultComponentContext

@Composable
fun RootView(rootComponentContext: DefaultComponentContext, colorScheme: ColorScheme?) {
    ProvideComponentContext(rootComponentContext) {
        setup()

        Content(componentContext = rootComponentContext, colorScheme = colorScheme)
    }
}
