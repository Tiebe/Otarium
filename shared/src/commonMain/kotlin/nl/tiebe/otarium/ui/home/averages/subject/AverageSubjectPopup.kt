package nl.tiebe.otarium.ui.home.averages.subject

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.tiebe.magisterapi.response.general.year.grades.Subject
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.logic.root.home.children.averages.AveragesComponent
import nl.tiebe.otarium.magister.GradeWithGradeInfo
import nl.tiebe.otarium.ui.home.averages.cards.AverageCalculator
import nl.tiebe.otarium.ui.home.averages.cards.graph.AverageGraph
import nl.tiebe.otarium.utils.calculateAverage
import nl.tiebe.otarium.utils.ui.format
import nl.tiebe.otarium.utils.ui.getLocalizedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AverageSubjectPopupTopAppBar(component: AveragesComponent, subject: Subject) {
    val manualGradeList = component.manualGradesList.subscribeAsState().value.filter { subject.id == -1 || it.subjectId == subject.id }
    val gradeList = component.gradesList.subscribeAsState().value.filter { subject.id == -1 || it.grade.subject.id == subject.id}.map {
            (it.grade.grade?.replace(',', '.')?.toFloatOrNull() ?: 0f) to it.gradeInfo.weight.toFloat()
        } + manualGradeList.map {
            (it.grade.toFloatOrNull() ?: 0f) to it.weight
        }

    TopAppBar(
        title = { Text(subject.description.capitalize(Locale.current)) },
        navigationIcon = {
            IconButton(onClick = { component.back() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            ElevatedCard(modifier = Modifier.size(40.dp)) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    val average = derivedStateOf { calculateAverage(gradeList) }

                    Text(
                        text = average.value.format(Data.decimals),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        color = if (Data.markGrades && average.value < Data.passingGrade) MaterialTheme.colorScheme.error else Color.Unspecified,
                    )
                }
            }
        },
        windowInsets = WindowInsets(0)
    )
}

@Composable
internal fun AverageSubjectPopup(component: AveragesComponent, subject: Subject, realGradeList: List<GradeWithGradeInfo>) {
    val gradeList = realGradeList.filter { subject.id == -1 || it.grade.subject.id == subject.id }
    val manualGradeList = component.manualGradesList.subscribeAsState().value.filter { subject.id == -1 || it.subjectId == subject.id }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        AverageGraph(grades = gradeList, manualGrades = manualGradeList, modifier = Modifier.padding(10.dp))

        AverageCalculator(grades = gradeList, manualGrades = manualGradeList)

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

        gradeList.reversed().forEach { grade ->
            GradeListItem(grade)
        }
    }
}
