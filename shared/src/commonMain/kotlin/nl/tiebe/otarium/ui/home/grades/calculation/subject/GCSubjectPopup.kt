package nl.tiebe.otarium.ui.home.grades.calculation.subject

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.tiebe.magisterapi.response.general.year.grades.Subject
import kotlinx.datetime.toLocalDateTime
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.magister.GradeWithGradeInfo
import nl.tiebe.otarium.ui.home.grades.calculation.GradeCalculationChildComponent
import nl.tiebe.otarium.ui.home.grades.calculation.calculateAverage
import nl.tiebe.otarium.ui.home.grades.calculation.cards.GCAverageCalculator
import nl.tiebe.otarium.ui.home.grades.calculation.cards.graph.GCGraph
import nl.tiebe.otarium.ui.utils.BackButton
import nl.tiebe.otarium.ui.utils.topBottomRectBorder
import nl.tiebe.otarium.utils.toFormattedString
import nl.tiebe.otarium.utils.ui.format

@Composable
internal fun GCSubjectPopup(component: GradeCalculationChildComponent, subject: Subject, gradeList: List<GradeWithGradeInfo>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
            Text(
                text = subject.description.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase() else it.toString()
                },
                maxLines = 1,
                style = MaterialTheme.typography.headlineMedium,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.7f).align(Alignment.Center)
            )

            ElevatedCard(modifier = Modifier.size(50.dp).align(Alignment.CenterEnd)) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = calculateAverage(gradeList).format(Data.decimals),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        GCGraph(grades = gradeList)

        GCAverageCalculator(grades = gradeList)

        GradeList(grades = gradeList)
    }

    Box(Modifier.fillMaxSize().padding(top = 12.dp, start = 12.dp)) {
        BackButton(
            Modifier.align(Alignment.TopStart),
            {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                )
            }) {
            component.closeSubject()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GradeList(grades: List<GradeWithGradeInfo>) {
    grades.reversed().forEach { grade ->
        ListItem(
            modifier = Modifier
                .topBottomRectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline)),
            headlineText = { Text(grade.gradeInfo.columnDescription ?: "") },
            supportingText = { Text(grade.grade.dateEntered?.substring(0, 26)?.toLocalDateTime()?.toFormattedString() ?: "") },
            trailingContent = {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                ) {
                    Text(
                        text = grade.grade.grade ?: "",
                        modifier = Modifier
                            .align(Alignment.Center),
                        style = MaterialTheme.typography.displaySmall.copy(fontSize = 18.sp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "${grade.gradeInfo.weight}x",
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                    )
                }
            },
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.inverseOnSurface
            ),
        )
    }
}