package nl.tiebe.otarium.android.ui.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Suppress("UnnecessaryComposedModifier")
fun Modifier.topBottomRectBorder(
    width: Dp = Dp.Hairline,
    brush: Brush = SolidColor(Color.LightGray)
): Modifier = composed(
    factory = {
        this.then(
            Modifier.drawWithCache {
                onDrawWithContent {
                    drawContent()
                    drawLine(
                        brush,
                        Offset(0f, 0f - width.value),
                        Offset(size.width, 0f - width.value)
                    )
                    drawLine(
                        brush,
                        Offset(0f, size.height - width.value),
                        Offset(size.width, size.height - width.value)
                    )
                }
            }
        )
    },
    inspectorInfo = debugInspectorInfo {
        name = "border"
        properties["width"] = width
        if (brush is SolidColor) {
            properties["color"] = brush.value
            value = brush.value
        } else {
            properties["brush"] = brush
        }
        properties["shape"] = RectangleShape
    }
)

@Suppress("UnnecessaryComposedModifier")
fun Modifier.topRectBorder(
    width: Dp = Dp.Hairline,
    brush: Brush = SolidColor(Color.LightGray)
): Modifier = composed(
    factory = {
        this.then(
            Modifier.drawWithCache {
                onDrawWithContent {
                    drawContent()
                    drawLine(
                        brush,
                        Offset(width.value, size.height + width.value),
                        Offset(411.5.dp.toPx(), size.height + width.value)
                    )
                    drawLine(
                        brush,
                        Offset(size.width, 0f - width.value),
                        Offset(size.width, size.height - width.value)
                    )
                }
            }
        )
    },
    inspectorInfo = debugInspectorInfo {
        name = "border"
        properties["width"] = width
        if (brush is SolidColor) {
            properties["color"] = brush.value
            value = brush.value
        } else {
            properties["brush"] = brush
        }
        properties["shape"] = RectangleShape
    }
)
