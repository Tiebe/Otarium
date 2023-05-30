package nl.tiebe.otarium.ui.utils

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun BoxScope.DownloadIndicator(progress: Float) {
    if (progress != 0f && !progress.isNaN()) {
        val color = MaterialTheme.colorScheme.primary
        val trackColor = MaterialTheme.colorScheme.surfaceVariant

        if (progress != 1f) {
            DownloadingFileIndicator(
                progress = progress,
                modifier = Modifier.matchParentSize().align(Alignment.BottomStart),
                color = color,
                trackColor = trackColor,
                height = 5.dp
            )
        } else {
            LoadingFileIndicator(
                modifier = Modifier.matchParentSize().align(Alignment.BottomStart),
                color = color,
                trackColor = trackColor,
                height = 5.dp
            )
        }

    }
}

fun DrawScope.drawLinearIndicator(
    startFraction: Float,
    endFraction: Float,
    color: Color,
    strokeWidth: Float
) {
    val width = size.width
    val height = size.height

    val isLtr = layoutDirection == LayoutDirection.Ltr
    val barStart = (if (isLtr) startFraction else 1f - endFraction) * width
    val barEnd = (if (isLtr) endFraction else 1f - startFraction) * width

    // Progress line
    drawLine(color, Offset(barStart, height), Offset(barEnd, height), strokeWidth)
}

fun DrawScope.drawLinearIndicatorTrack(
    color: Color,
    strokeWidth: Float
) = drawLinearIndicator(0f, 1f, color, strokeWidth)


@Composable
fun DownloadingFileIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color,
    trackColor: Color,
    height: Dp
) {
    Canvas(
        modifier
            .progressSemantics(progress)
    ) {
        drawLinearIndicatorTrack(trackColor, height.toPx())
        drawLinearIndicator(0f, progress, color, height.toPx())
    }
}

@Composable
fun LoadingFileIndicator(
    modifier: Modifier = Modifier,
    color: Color,
    trackColor: Color,
    height: Dp
) {
    val infiniteTransition = rememberInfiniteTransition()
    // Fractional position of the 'head' and 'tail' of the two lines drawn, i.e. if the head is 0.8
    // and the tail is 0.2, there is a line drawn from between 20% along to 80% along the total
    // width.
    val firstLineHead = infiniteTransition.animateFloat(
        0f,
        1f,
        infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1800
                0f at 0 with CubicBezierEasing(0.2f, 0f, 0.8f, 1f)
                1f at 750 + 0
            }
        )
    )
    val firstLineTail = infiniteTransition.animateFloat(
        0f,
        1f,
        infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1800
                0f at 333 with CubicBezierEasing(0.4f, 0f, 1f, 1f)
                1f at 850 + 333
            }
        )
    )
    val secondLineHead = infiniteTransition.animateFloat(
        0f,
        1f,
        infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1800
                0f at 1000 with CubicBezierEasing(0f, 0f, 0.65f, 1f)
                1f at 567 + 1000
            }
        )
    )
    val secondLineTail = infiniteTransition.animateFloat(
        0f,
        1f,
        infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1800
                0f at 1267 with CubicBezierEasing(0.1f, 0f, 0.45f, 1f)
                1f at 533 + 1267
            }
        )
    )
    Canvas(
        modifier
            .progressSemantics()
    ) {
        val strokeWidth = height.toPx()
        drawLinearIndicatorTrack(trackColor, strokeWidth)
        if (firstLineHead.value - firstLineTail.value > 0) {
            drawLinearIndicator(
                firstLineHead.value,
                firstLineTail.value,
                color,
                strokeWidth
            )
        }
        if (secondLineHead.value - secondLineTail.value > 0) {
            drawLinearIndicator(
                secondLineHead.value,
                secondLineTail.value,
                color,
                strokeWidth
            )
        }
    }
}