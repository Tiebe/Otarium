package dev.tiebe.otarium.ui.home.grades.calculation.subject

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.tiebe.magisterapi.response.general.year.grades.Subject
import dev.tiebe.otarium.Data
import dev.tiebe.otarium.MR
import dev.tiebe.otarium.magister.GradeWithGradeInfo
import dev.tiebe.otarium.logic.home.children.grades.children.calculation.GradeCalculationChildComponent
import dev.tiebe.otarium.logic.home.children.grades.children.calculation.calculateAverage
import dev.tiebe.otarium.ui.home.grades.calculation.cards.GCAverageCalculator
import dev.tiebe.otarium.ui.home.grades.calculation.cards.graph.GCGraph
import dev.tiebe.otarium.ui.utils.BackButton
import dev.tiebe.otarium.utils.ui.format
import dev.tiebe.otarium.utils.ui.getLocalizedString

@Composable
internal fun GCSubjectPopup(component: GradeCalculationChildComponent, subject: Subject, realGradeList: List<GradeWithGradeInfo>) {
    val manualGradeList = component.manualGradesList.subscribeAsState().value.filter { it.subjectId == subject.id }
    val gradeList = derivedStateOf {
        realGradeList.map {
            (it.grade.grade?.replace(',', '.')?.toFloatOrNull() ?: 0f) to it.gradeInfo.weight.toFloat()
        } + manualGradeList.map {
            (it.grade.toFloatOrNull() ?: 0f) to it.weight
        }
    }

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
                    val average = derivedStateOf { calculateAverage(gradeList.value) }

                    Text(
                        text = average.value.format(Data.decimals),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        color = if (Data.markGrades && average.value < Data.passingGrade) MaterialTheme.colorScheme.error else Color.Unspecified,
                    )
                }
            }
        }

        GCGraph(grades = realGradeList, manualGrades = manualGradeList)

        GCAverageCalculator(grades = realGradeList, manualGrades = manualGradeList)

        val addItemPopout = component.addManualGradePopupOpen.subscribeAsState().value

        Row(Modifier.padding(top = 20.dp, start = 20.dp, bottom = 12.dp)) {
            Text(
                text = getLocalizedString(MR.strings.manual_grades),
                style = MaterialTheme.typography.headlineSmall,
            )

            Spacer(modifier = Modifier.weight(1f))

            val rotation by animateFloatAsState(
                targetValue = if (addItemPopout) 45f else 0f
            )

            IconButton(onClick = {
                component.addManualGradePopupOpen.value = !addItemPopout
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add grade manually",
                    modifier = Modifier.size(24.dp).rotate(rotation)
                )
            }
        }


        AnimatedVisibility(visible = addItemPopout, enter = expandVertically(), exit = shrinkVertically()) {
            Column {
                AddGradeManually(component, subject)
            }
        }

        manualGradeList.reversed().forEach {
            ManualGradeListItem(it, component)
        }

        Text(
            text = getLocalizedString(MR.strings.real_grades),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(20.dp)
        )

        realGradeList.reversed().forEach { grade ->
            GradeListItem(grade)
        }
    }

    Box(Modifier.fillMaxSize().padding(top = 20.dp, start = 20.dp, bottom = 12.dp)) {
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
