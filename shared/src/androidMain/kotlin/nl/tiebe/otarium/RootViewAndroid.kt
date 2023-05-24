package nl.tiebe.otarium

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.DefaultComponentContext

@Composable
fun RootView(rootComponentContext: DefaultComponentContext, lightColorScheme: ColorScheme?, darkColorScheme: ColorScheme?, padding: PaddingValues) {
    ProvideComponentContext(rootComponentContext) {
        setup()

        Content(componentContext = rootComponentContext, lightColorScheme, darkColorScheme, padding = padding)
    }
}
