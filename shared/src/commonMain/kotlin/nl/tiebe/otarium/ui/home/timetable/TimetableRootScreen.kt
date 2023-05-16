package nl.tiebe.otarium.ui.home.timetable

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.toSize
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.StackAnimator
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimator
import nl.tiebe.otarium.ui.home.timetable.item.TimetableItemPopup
import nl.tiebe.otarium.ui.home.timetable.main.TimetableScreen

@Composable
internal fun TimetableRootScreen(component: TimetableRootComponent) {
    TimetableScreen(component.timetableComponent)

    var size by remember { mutableStateOf(Size.Zero) }

    val offsetX = remember { mutableStateOf(0f) }
    val offsetY = remember { mutableStateOf(0f) }


    var factor by remember { mutableStateOf(0f) }

    Children(
        stack = component.childStack,
        animation = stackAnimation(animator = fade(), disableInputDuringAnimation = false),
        modifier = Modifier.fillMaxSize()
            .onSizeChanged { size = it.toSize() }
    ) {
        when (val child = it.instance) {
            is TimetableRootComponent.Child.TimetableChild -> {}
            is TimetableRootComponent.Child.TimetablePopupChild -> TimetableItemPopup(child.component, child.id, Modifier)/*.pointerInput(Unit) {
                detectDragGestures(onDragStart = { position ->
                    offsetX.value = position.x
                    offsetY.value = position.y
                }) { change, dragAmount ->
                    change.consume()

                    val original = Offset(offsetX.value, offsetY.value)
                    val summed = original + dragAmount
                    val newValue = Offset(
                        x = summed.x.coerceIn(0f, size.width - 50.dp.toPx()),
                        y = summed.y.coerceIn(0f, size.height - 50.dp.toPx())
                    )
                    offsetX.value = newValue.x
                    offsetY.value = newValue.y
                    
                    factor = newValue.x / (size.width)
                }
            }.size(size.width.dp, size.height.dp).offset(x = offsetX.value.dp, y = offsetY.value.dp))*/
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
