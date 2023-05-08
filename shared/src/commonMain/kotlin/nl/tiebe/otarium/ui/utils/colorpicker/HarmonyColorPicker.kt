package nl.tiebe.otarium.ui.utils.colorpicker

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.*


@Deprecated(
    message = "Use version with programmatic updates",
    replaceWith = ReplaceWith(
        """
            var updatedHsvColor by remember(color) {
               mutableStateOf(HsvColor.from(color))
            }

            HarmonyColorPicker(
                modifier = modifier,
                harmonyMode = harmonyMode,
                value = updatedHsvColor,
                onValueChanged = { hsvColor ->
                    updatedHsvColor = hsvColor
                    onColorChanged(hsvColor)
                },
            )
            """
    )
)
@Composable
fun HarmonyColorPicker(
    modifier: Modifier = Modifier,
    harmonyMode: ColorHarmonyMode,
    color: Color = Color.Red,
    onColorChanged: (HsvColor) -> Unit
) {
    var updatedHsvColor by remember(color) {
        mutableStateOf(HsvColor.from(color))
    }
    HarmonyColorPicker(
        modifier = modifier,
        harmonyMode = harmonyMode,
        color = updatedHsvColor,
        onColorChanged = { hsvColor ->
            updatedHsvColor = hsvColor
            onColorChanged(hsvColor)
        }
    )
}

/**
 *
 * Will show a brightness bar if [showBrightnessBar] is true
 * otherwise all colors are given the provided brightness value
 */
@Composable
fun HarmonyColorPicker(
    modifier: Modifier = Modifier,
    harmonyMode: ColorHarmonyMode,
    color: HsvColor,
    showBrightnessBar: Boolean = true,
    onColorChanged: (HsvColor) -> Unit
) {
    BoxWithConstraints(modifier) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            val updatedColor by rememberUpdatedState(color)
            val updatedOnValueChanged by rememberUpdatedState(onColorChanged)

            HarmonyColorPickerWithMagnifiers(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f),
                hsvColor = updatedColor,
                onColorChanged = {
                    updatedOnValueChanged(it)
                },
                harmonyMode = harmonyMode
            )

            if (showBrightnessBar) {
                BrightnessBar(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .weight(0.2f),
                    onValueChange = { value ->
                        updatedOnValueChanged(updatedColor.copy(value = value))
                    },
                    currentColor = updatedColor
                )
            }
        }
    }
}

@Composable
private fun HarmonyColorPickerWithMagnifiers(
    modifier: Modifier = Modifier,
    hsvColor: HsvColor,
    onColorChanged: (HsvColor) -> Unit,
    harmonyMode: ColorHarmonyMode
) {
    val hsvColorUpdated by rememberUpdatedState(hsvColor)
    BoxWithConstraints(
        modifier = modifier
            .defaultMinSize(minWidth = 48.dp)
            .wrapContentSize()
            .aspectRatio(1f, matchHeightConstraintsFirst = true)

    ) {
        val updatedOnColorChanged by rememberUpdatedState(onColorChanged)
        val diameterPx by remember(constraints.maxWidth) {
            mutableStateOf(constraints.maxWidth)
        }

        var animateChanges by remember {
            mutableStateOf(false)
        }
        var currentlyChangingInput by remember {
            mutableStateOf(false)
        }

        fun updateColorWheel(newPosition: Offset, animate: Boolean) {
            // Work out if the new position is inside the circle we are drawing, and has a
            // valid color associated to it. If not, keep the current position
            val newColor = colorForPosition(newPosition, IntSize(diameterPx, diameterPx), hsvColorUpdated.value)
            if (newColor != null) {
                animateChanges = animate
                updatedOnColorChanged(newColor)
            }
        }

        val inputModifier = Modifier.pointerInput(diameterPx) {
            forEachGesture {
                awaitPointerEventScope {
                    val down = awaitFirstDown(false)
                    currentlyChangingInput = true
                    updateColorWheel(down.position, animate = true)
                    drag(down.id) { change ->
                        updateColorWheel(change.position, animate = false)
                        change.consumePositionChange()
                    }
                    currentlyChangingInput = false
                }
            }
        }

        Box(inputModifier.fillMaxSize()) {
            ColorWheel(hsvColor = hsvColor, diameter = diameterPx)
            HarmonyColorMagnifiers(
                diameterPx,
                hsvColor,
                animateChanges,
                currentlyChangingInput,
                harmonyMode
            )
        }
    }
}

private fun colorForPosition(position: Offset, size: IntSize, value: Float): HsvColor? {
    val centerX: Double = size.width / 2.0
    val centerY: Double = size.height / 2.0
    val radius: Double = min(centerX, centerY)
    val xOffset: Double = position.x - centerX
    val yOffset: Double = position.y - centerY
    val centerOffset = hypot(xOffset, yOffset)
    val rawAngle = atan2(yOffset, xOffset).toDegree()
    val centerAngle = (rawAngle + 360.0) % 360.0
    return if (centerOffset <= radius) {
        HsvColor(
            hue = centerAngle.toFloat(),
            saturation = (centerOffset / radius).toFloat(),
            value = value,
            alpha = 1.0f
        )
    } else {
        null
    }
}

@Composable
internal fun BrightnessBar(
    modifier: Modifier = Modifier,
    onValueChange: (Float) -> Unit,
    currentColor: HsvColor
) {
    Slider(
        modifier = modifier,
        value = currentColor.value,
        onValueChange = {
            onValueChange(it)
        },
        colors = SliderDefaults.colors(
            activeTrackColor = MaterialTheme.colorScheme.primary,
            thumbColor = MaterialTheme.colorScheme.primary
        )
    )
}


@Composable
internal fun HarmonyColorMagnifiers(
    diameterPx: Int,
    hsvColor: HsvColor,
    animateChanges: Boolean,
    currentlyChangingInput: Boolean,
    harmonyMode: ColorHarmonyMode
) {
    val size = IntSize(diameterPx, diameterPx)
    val position = remember(hsvColor, size) {
        positionForColor(hsvColor, size)
    }

    val positionAnimated = remember {
        Animatable(position, typeConverter = Offset.VectorConverter)
    }
    LaunchedEffect(hsvColor, size, animateChanges) {
        if (!animateChanges) {
            positionAnimated.snapTo(positionForColor(hsvColor, size))
        } else {
            positionAnimated.animateTo(
                positionForColor(hsvColor, size),
                animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
            )
        }
    }

    val diameterDp = with(LocalDensity.current) {
        diameterPx.toDp()
    }

    val animatedDiameter = animateDpAsState(
        targetValue = if (!currentlyChangingInput) {
            diameterDp * diameterMainColorDragging
        } else {
            diameterDp * diameterMainColor
        }
    )

    hsvColor.getColors(harmonyMode).forEach { color ->
        val positionForColor = remember {
            Animatable(positionForColor(color, size), typeConverter = Offset.VectorConverter)
        }
        LaunchedEffect(color, size, animateChanges) {
            if (!animateChanges) {
                positionForColor.snapTo(positionForColor(color, size))
            } else {
                positionForColor.animateTo(
                    positionForColor(color, size),
                    animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
                )
            }
        }
        Magnifier(position = positionForColor.value, color = color, diameter = diameterDp * diameterHarmonyColor)
    }
    Magnifier(position = positionAnimated.value, color = hsvColor, diameter = animatedDiameter.value)
}

@Composable
internal fun Magnifier(position: Offset, color: HsvColor, diameter: Dp) {
    val offset = with(LocalDensity.current) {
        Modifier.offset(
            position.x.toDp() - diameter / 2,
            // Align with the center of the selection circle
            position.y.toDp() - diameter / 2
        )
    }

    Column(offset.size(width = diameter, height = diameter)) {
        MagnifierSelectionCircle(Modifier.size(diameter), color)
    }
}

/**
 * Selection circle drawn over the currently selected pixel of the color wheel.
 */
@Composable
private fun MagnifierSelectionCircle(modifier: Modifier, color: HsvColor) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        tonalElevation = 4.dp,
        color = color.toColor(),
        border = BorderStroke(2.dp, SolidColor(Color.White)),
        content = {}
    )
}

internal fun positionForColor(color: HsvColor, size: IntSize): Offset {
    val radians = color.hue.toRadian()
    val phi = color.saturation
    val x: Float = ((phi * cos(radians)) + 1) / 2f
    val y: Float = ((phi * sin(radians)) + 1) / 2f
    return Offset(
        x = (x * size.width),
        y = (y * size.height)
    )
}

private const val diameterHarmonyColor = 0.10f
private const val diameterMainColorDragging = 0.18f
private const val diameterMainColor = 0.15f

internal fun Float.toRadian(): Float = this / 180.0f * PI.toFloat()
internal fun Double.toRadian(): Double = this / 180 * PI
internal fun Float.toDegree(): Float = this * 180.0f / PI.toFloat()
internal fun Double.toDegree(): Double = this * 180 / PI