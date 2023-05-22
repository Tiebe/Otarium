package nl.tiebe.otarium.ui.home.timetable

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.StackAnimator
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimator
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.essenty.backhandler.BackCallback
import kotlinx.coroutines.launch
import nl.tiebe.otarium.ui.home.timetable.item.TimetableItemPopup
import nl.tiebe.otarium.ui.home.timetable.main.TimetableScreen

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
        background = {
        }
    ) {
        if (child is TimetableRootComponent.Child.TimetablePopupChild) {
            TimetableItemPopup(child.component, child.id, Modifier)
        }
    }

}

@Composable
internal fun slide(animationSpec: FiniteAnimationSpec<Float> = tween(), factor: Float): StackAnimator {
    //println(factor)

    val animator = stackAnimator(animationSpec = animationSpec) { animationFactor, _, content ->
        content(Modifier.offsetXFactor(factor = maxOf(animationFactor, factor)))
    }

    return animator
}

private fun Modifier.offsetXFactor(factor: Float): Modifier =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)

        layout(placeable.width, placeable.height) {
            placeable.placeRelative(x = (placeable.width.toFloat() * factor).toInt(), y = 0)
        }
    }
