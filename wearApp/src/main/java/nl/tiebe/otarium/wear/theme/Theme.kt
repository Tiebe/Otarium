package nl.tiebe.otarium.wear.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material3.MaterialTheme

@Composable
fun OtariumTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
//        colors = defaultDarkTheme,
        content = content
    )
}

//fun CustomTheme.toColors(): Colors {
//    return Colors(
//        primary
//    )
//}