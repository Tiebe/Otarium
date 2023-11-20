package nl.tiebe.otarium.ui.home.averages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.logic.root.home.children.averages.AveragesComponent
import nl.tiebe.otarium.ui.home.averages.subject.AverageSubjectPopup

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun AveragesScreen(component: AveragesComponent) {
    val grades = component.gradesList.subscribeAsState().value
    val manualGrades = component.manualGradesList.subscribeAsState().value


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
    val refreshState = rememberPullRefreshState(component.refreshState.subscribeAsState().value, { component.refreshGrades() })

    Box(Modifier.pullRefresh(refreshState)) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            subjects.forEach { subject ->
                SubjectCard(
                    component,
                    subject,
                    grades.filter { it.grade.subject.id == subject.id },
                    manualGrades.filter { it.subjectId == subject.id }
                )

                /*ListItem(
                    modifier = Modifier
                        .rectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline))
                        .clickable { component.openSubject(subject) },
                    headlineContent = {
                        Text(subject.description.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase() else it.toString()
                        })
                    },
                    trailingContent = {
                        val average = derivedStateOf { calculateAverage(gradeList) }

                        Text(
                            text = average.value.format(1),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(2.dp),
                            color = if (Data.markGrades && average.value < Data.passingGrade) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.displaySmall.copy(fontSize = 18.sp)
                        )
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.inverseOnSurface
                    ),
                )*/
            }
        }

        PullRefreshIndicator(
            refreshing = component.refreshState.subscribeAsState().value,
            state = refreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
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
            androidx.compose.material3.Surface(Modifier.fillMaxSize()) {
                AverageSubjectPopup(
                    component = component,
                    subject = popupItem.value.second!!,
                    realGradeList = grades.filter { it.grade.subject.id == popupItem.value.second?.id }
                )
            }
        }
    }
}
