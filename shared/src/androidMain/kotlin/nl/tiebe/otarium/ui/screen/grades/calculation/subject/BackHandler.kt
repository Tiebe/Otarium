package nl.tiebe.otarium.ui.screen.grades.calculation.subject

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun CustomBackHandler(onBack: () -> Unit) {
    BackHandler {
        onBack()
    }
}