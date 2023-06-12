package dev.tiebe.otarium

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.DefaultComponentContext

@Composable
fun RootView(rootComponentContext: DefaultComponentContext, lightColorScheme: ColorScheme?, darkColorScheme: ColorScheme?, padding: WindowInsets) {
    ProvideComponentContext(rootComponentContext) {
        setup()

        Content(componentContext = rootComponentContext, lightColorScheme, darkColorScheme, padding = padding)
    }
}
