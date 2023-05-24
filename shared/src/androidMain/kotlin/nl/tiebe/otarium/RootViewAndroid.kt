package nl.tiebe.otarium

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.DefaultComponentContext

@Composable
fun RootView(rootComponentContext: DefaultComponentContext, colorScheme: ColorScheme?, padding: PaddingValues) {
    ProvideComponentContext(rootComponentContext) {
        setup()

        Content(componentContext = rootComponentContext, colorScheme = colorScheme, padding = padding)
    }
}
