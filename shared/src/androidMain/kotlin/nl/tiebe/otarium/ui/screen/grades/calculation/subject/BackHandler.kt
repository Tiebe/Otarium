package nl.tiebe.otarium.ui.screen.grades.calculation.subject

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import com.arkivanov.essenty.backhandler.BackHandler

@Composable
internal actual fun CustomBackHandler(onBack: () -> Unit) {
    BackHandler(OnBackPressedDispatcher {
        onBack()
    })
}