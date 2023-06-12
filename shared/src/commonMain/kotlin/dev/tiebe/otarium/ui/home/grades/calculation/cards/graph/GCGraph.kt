package dev.tiebe.otarium.ui.home.grades.calculation.cards.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.tiebe.otarium.MR
import dev.tiebe.otarium.magister.GradeWithGradeInfo
import dev.tiebe.otarium.magister.ManualGrade
import dev.tiebe.otarium.logic.home.children.grades.children.calculation.calculateAverage
import dev.tiebe.otarium.logic.home.children.grades.children.calculation.calculateAverageGrade
import dev.tiebe.otarium.utils.ui.getLocalizedString

@OptIn(ExperimentalTextApi::class)
@Composable
internal fun GCGraph(grades: List<GradeWithGradeInfo>, manualGrades: List<ManualGrade>) {
    ElevatedCard(
        modifier = Modifier.padding(10.dp),
    ) {
        Text(
            text = getLocalizedString(MR.strings.graph),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        val textColor = MaterialTheme.colorScheme.onBackground
        val gradeLineColor = MaterialTheme.colorScheme.secondary
        val averageLineColor = MaterialTheme.colorScheme.tertiary
        val axisLineColor = MaterialTheme.colorScheme.outline

        val lineBound = remember { mutableStateOf(1F) }

        val gradeText = getLocalizedString(MR.strings.grade)
        val averageText = getLocalizedString(MR.strings.average)

        val density = LocalDensity.current
        val fontFamilyResolver = LocalFontFamilyResolver.current

        val textStyle = MaterialTheme.typography.labelSmall.copy(color = textColor)

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(top = 20.dp, start = 30.dp)
                .drawBehind { drawAxis(lineColor = axisLineColor, textColor = textColor, textStyle = textStyle, fontFamilyResolver = fontFamilyResolver, density = density) }
                .padding(horizontal = 4.dp)

        ) {
            lineBound.value = size.width / (grades.size + manualGrades.size) * 0.8f

            val gradeBrush = Brush.linearGradient(listOf(gradeLineColor, gradeLineColor))
            val gradePath = Path().apply { moveTo(0f, size.height) }

            val averageBrush = Brush.linearGradient(listOf(averageLineColor, averageLineColor))
            val averagePath = Path().apply { moveTo(0f, size.height) }

            grades.forEachIndexed { index, gradeInfo ->
                val grade = gradeInfo.grade.grade?.replace(',', '.')?.toFloatOrNull() ?: 0f
                val average = calculateAverageGrade(grades.subList(0, index+1))

                drawGrade(index, lineBound, grade, average, grades.size + manualGrades.size, gradePath, averagePath, gradeBrush, averageBrush)
            }

            manualGrades.forEachIndexed { index, manualGrade ->
                val grade = manualGrade.grade.replace(',', '.').toFloatOrNull() ?: 0f
                val average = calculateAverage(
                    grades.map {
                    (it.grade.grade?.replace(',', '.')?.toFloatOrNull() ?: 0f) to it.gradeInfo.weight.toFloat()
                } + manualGrades.subList(0, index+1).map {
                    (it.grade.toFloatOrNull() ?: 0f) to it.weight
                })

                drawGrade(index + grades.size, lineBound, grade, average, grades.size + manualGrades.size, gradePath, averagePath, gradeBrush, averageBrush)
            }

            if (grades.size + manualGrades.size > 1) {
                drawPath(
                    path = gradePath,
                    brush = gradeBrush,
                    style = Stroke(width = size.width / 100),
                )

                drawPath(
                    path = averagePath,
                    brush = averageBrush,
                    style = Stroke(width = size.width / 100),
                )
            }

            drawCircle(
                center = Offset(size.width - 200,size.height - 100),
                brush = gradeBrush,
                radius = size.width / 70
            )

            drawCircle(
                center = Offset(size.width - 200,size.height - 50),
                brush = averageBrush,
                radius = size.width / 70
            )

            drawIntoCanvas {
                it.nativeCanvas.apply {
                    drawText(
                        textMeasurer = TextMeasurer(fontFamilyResolver, density, LayoutDirection.Ltr),
                        text = gradeText,
                        topLeft = Offset(size.width - 170, size.height - 120),
                        style = textStyle
                    )

                    drawText(
                        textMeasurer = TextMeasurer(fontFamilyResolver, density, LayoutDirection.Ltr),
                        text = averageText,
                        topLeft = Offset(size.width - 170, size.height - 70),
                        style = textStyle
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawGrade(
    index: Int,
    lineBound: MutableState<Float>,
    grade: Float,
    average: Float,
    listSize: Int,
    gradePath: Path,
    averagePath: Path,
    gradeBrush: Brush,
    averageBrush: Brush
) {
    val gradeOffset = Offset((2 * index + 1) * lineBound.value * 0.6f, size.height - grade * (size.height / 10))
    val averageOffset = Offset((2 * index + 1) * lineBound.value * 0.6f, size.height - average * (size.height / 10))

    if (listSize > 1) {
        when (index) {
            0 -> {
                gradePath.moveTo(gradeOffset.x, gradeOffset.y)
                averagePath.moveTo(averageOffset.x, averageOffset.y)
            }

            else -> {
                gradePath.lineTo(gradeOffset.x, gradeOffset.y)
                averagePath.lineTo(averageOffset.x, averageOffset.y)
            }

        }
    }

    drawCircle(
        center = gradeOffset,
        radius = size.width / 70,
        brush = gradeBrush
    )

    drawCircle(
        center = averageOffset,
        radius = size.width / 70,
        brush = averageBrush
    )
}