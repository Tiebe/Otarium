package nl.tiebe.otarium.ui.home.averages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.logic.root.home.children.averages.AveragesComponent
import nl.tiebe.otarium.ui.home.averages.subject.AverageSubjectPopup
import nl.tiebe.otarium.ui.utils.BackButton
import nl.tiebe.otarium.utils.OtariumIcons
import nl.tiebe.otarium.utils.otariumicons.Cards
import nl.tiebe.otarium.utils.otariumicons.Down
import nl.tiebe.otarium.utils.otariumicons.List
import nl.tiebe.otarium.utils.ui.getLocalizedString

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
internal fun AveragesScreen(component: AveragesComponent) {
    Column(Modifier.fillMaxSize()) {

        val grades = component.gradesList.subscribeAsState().value
        val manualGrades = component.manualGradesList.subscribeAsState().value

        val subjects = grades.map { it.grade.subject }.distinct().sortedBy { it.description.lowercase() }

        val popupItem = component.openedSubject.subscribeAsState()
        val state = rememberDismissState(DismissValue.DismissedToEnd)

        LaunchedEffect(popupItem.value) {
            if (popupItem.value.first) {
                state.dismiss(DismissDirection.EndToStart)
            } else {
                state.dismiss(DismissDirection.StartToEnd)
            }
        }

        LaunchedEffect(state.currentValue) {
            if (state.currentValue != DismissValue.Default) {
                component.closeSubject()
            }
        }
        val refreshState =
            rememberPullRefreshState(component.refreshState.subscribeAsState().value, { component.refreshGrades() })

        val cardList = remember { mutableStateOf(Data.cardList) }

        TopAppBar(
            title = { Text(getLocalizedString(MR.strings.averagesItem)) },
            windowInsets = WindowInsets(0),
            actions = {
                IconButton(onClick = {
                    cardList.value = !cardList.value
                    Data.cardList = cardList.value
                }) {
                    Icon(
                        imageVector = if (cardList.value) OtariumIcons.List else OtariumIcons.Cards,
                        contentDescription = null
                    )
                }
            }
        )


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
                        manualGrades.filter { it.subjectId == subject.id },
                        cardList.value
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
            directions = setOf(DismissDirection.StartToEnd),
            background = {},
            dismissContent =  {
            if (popupItem.value.first) {
                Surface(Modifier.fillMaxSize()) {
                    AverageSubjectPopup(
                        component = component,
                        subject = popupItem.value.second!!,
                        realGradeList = grades.filter { it.grade.subject.id == popupItem.value.second?.id }
                    )
                }
            }
        })
    }
}
