package nl.tiebe.otarium.ui.home.grades.calculation.cards.graph

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

@OptIn(ExperimentalTextApi::class)
fun DrawScope.drawAxis(
    lineColor: Color = Color.Blue,
    textColor: Color = Color.White,
    textStyle: TextStyle,
    fontFamilyResolver: FontFamily.Resolver,
    density: Density
) {
    repeat(10) { index ->
        val position = size.height / 10 * index
        drawIntoCanvas {
            it.nativeCanvas.apply {
                drawText(
                    textMeasurer = TextMeasurer(fontFamilyResolver, density, LayoutDirection.Ltr),
                    text = (10-index).toString(),
                    topLeft = Offset(-30f - (((10-index).toString().length-1) * 15), position-20),
                    style = textStyle.copy(color = textColor)
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