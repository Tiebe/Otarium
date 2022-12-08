package nl.tiebe.otarium.ui.screen.grades.calculation.subject

import androidx.compose.runtime.Composable
import moe.tlaster.precompose.navigation.BackHandler

@Composable
internal actual fun CustomBackHandler(onBack: () -> Unit) {
    BackHandler {
        onBack()
    }
}