package nl.tiebe.otarium.ui.home.timetable

import androidx.compose.animation.core.tween
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.essenty.backhandler.BackCallback
import nl.tiebe.otarium.logic.root.home.children.timetable.TimetableRootComponent
import nl.tiebe.otarium.ui.home.timetable.item.TimetableItemPopup
import nl.tiebe.otarium.ui.home.timetable.main.TimetableScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun TimetableRootScreen(component: TimetableRootComponent) {
    val child = component.childStack.subscribeAsState().value.active.instance
    val state = rememberDismissState(DismissValue.DismissedToEnd)
    val scope = rememberCoroutineScope()

    LaunchedEffect(child) {
        if (child is TimetableRootComponent.Child.TimetablePopupChild) {
            component.onBack.value = BackCallback { scope.launch { state.animateTo(DismissValue.DismissedToEnd) } }
            component.registerBackHandler()

            state.animateTo(DismissValue.Default, tween())
        }
    }

    LaunchedEffect(state.currentValue) {
        if (state.currentValue != DismissValue.Default) {
            component.back()
            component.unregisterBackHandler()
        }
    }

    TimetableScreen(component.timetableComponent)

    SwipeToDismiss(
        state = state,
        dismissThresholds = { FractionalThreshold(0.5f) },
        directions = setOf(DismissDirection.StartToEnd),
        background = {
        }
    ) {
        if (child is TimetableRootComponent.Child.TimetablePopupChild) {
            TimetableItemPopup(child.component, child.id, Modifier)
        }
    }

}