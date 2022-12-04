package nl.tiebe.otarium.ui.screen.grades.calculation.cards.graph

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

fun DrawScope.drawAxis(
    lineColor: Color = Color.Blue,
    textColor: Color = Color.White
) {
    repeat(10) { index ->
        val position = size.height / 10 * index
/*        drawIntoCanvas {
            it.nativeCanvas.apply {
                drawTextLine(
                    TextLine.make((10-index).toString(), Font()),
                    -25f,
                    position + 10,
                    Paint().apply {
                        color = textColor.toArgb()
                        strokeWidth = size.width / 30
                    }
                )
            }
        }*/
        drawLine(
            start = Offset(x = 0f, y = position),
            end = Offset(x = size.width, y = position),
            color = lineColor,
            pathEffect = null,
            alpha = 0.1F,
            strokeWidth = size.width / 200
        )
    }
}