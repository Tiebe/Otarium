package nl.tiebe.otarium.ui.home.grades.calculation.subject

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.magister.GradeWithGradeInfo
import nl.tiebe.otarium.magister.ManualGrade
import nl.tiebe.otarium.ui.home.grades.calculation.GradeCalculationChildComponent
import nl.tiebe.otarium.ui.home.grades.calculation.calculateAverage
import nl.tiebe.otarium.ui.utils.topBottomRectBorder
import nl.tiebe.otarium.utils.ui.format


@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
)
@Composable
internal fun GCSubjectList(
    component: GradeCalculationChildComponent,
    grades: List<GradeWithGradeInfo>,
    manualGrades: List<ManualGrade>
) {
    val subjects = grades.map { it.grade.subject }.distinct().sortedBy { it.description.lowercase() }

    val popupItem = component.openedSubject.subscribeAsState()
    val state = rememberDismissState(DismissValue.DismissedToEnd)

    LaunchedEffect(popupItem.value) {
        if (popupItem.value.first) {
            state.animateTo(DismissValue.Default)
        } else {
            state.animateTo(DismissValue.DismissedToEnd)
        }
    }

    LaunchedEffect(state.currentValue) {
        if (state.currentValue != DismissValue.Default) {
            component.closeSubject()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        subjects.forEach { subject ->
            val gradeList = remember {
                grades.filter { it.grade.subject.id == subject.id }.map {
                    (it.grade.grade?.replace(',', '.')?.toFloatOrNull() ?: 0f) to it.gradeInfo.weight.toFloat()
                } + manualGrades.filter { it.subjectId == subject.id }.map {
                    (it.grade.toFloatOrNull() ?: 0f) to it.weight
                }
            }

            ListItem(
                modifier = Modifier
                    .topBottomRectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline))
                    .clickable { component.openSubject(subject) },
                headlineText = { Text(subject.description.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase() else it.toString()
                }) },
                trailingContent = {
                    val average = derivedStateOf { calculateAverage(gradeList) }

                    Text(
                        text = average.value.format(1),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(2.dp),
                        color = if (average.value < Data.passingGrade) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.displaySmall.copy(fontSize = 18.sp)
                    )
                },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface
                ),
            )
        }
    }

    SwipeToDismiss(
        state = state,
        dismissThresholds = {
            FractionalThreshold(0.5f)
        },
        directions = setOf(DismissDirection.StartToEnd),
        background = {

        }
    ) {
        if (popupItem.value.first) {
            Surface(Modifier.fillMaxSize()) {
                GCSubjectPopup(
                    component = component,
                    subject = popupItem.value.second!!,
                    realGradeList = grades.filter { it.grade.subject.id == popupItem.value.second?.id }
                )
            }
        }
    }

}