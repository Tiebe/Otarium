package nl.tiebe.otarium.ui.utils

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

@Suppress("UnnecessaryComposedModifier")
fun Modifier.rectBorder(
    width: Dp = Dp.Hairline,
    brush: Brush = SolidColor(Color.LightGray),
    drawLeft: Boolean = false,
    drawRight: Boolean = false,
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
                    if (drawLeft) drawLine(
                        brush,
                        Offset(0f, 0f),
                        Offset(0f, size.height)
                    )
                    if (drawRight) drawLine(
                        brush,
                        Offset(size.width, 0f),
                        Offset(size.width, size.height)
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
