package nl.tiebe.otarium.android.ui.screen.grades.calculation.graph

import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb

fun DrawScope.drawAxis(
    lineColor: Color = Color.Blue,
    textColor: Color = Color.White
) {
    repeat(10) { index ->
        val position = size.height / 10 * index
        drawIntoCanvas {
            it.nativeCanvas.apply {
                drawText(
                    (10-index).toString(),
                    -25f,
                    position + 10,
                    Paint().apply {
                        color = textColor.toArgb()
                        textSize = size.width / 30
                        textAlign = Paint.Align.CENTER
                    }
                )
            }
        }
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