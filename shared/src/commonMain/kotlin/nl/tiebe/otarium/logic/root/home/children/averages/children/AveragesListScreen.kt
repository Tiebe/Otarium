package nl.tiebe.otarium.logic.root.home.children.averages.children

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.tiebe.magisterapi.response.general.year.grades.Subject
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.logic.root.home.children.averages.AveragesComponent
import nl.tiebe.otarium.ui.home.grades.averages.SubjectCard
import nl.tiebe.otarium.utils.OtariumIcons
import nl.tiebe.otarium.utils.otariumicons.Cards
import nl.tiebe.otarium.utils.otariumicons.List
import nl.tiebe.otarium.utils.ui.getLocalizedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AveragesListScreenTopAppBar(component: AveragesComponent) {
    TopAppBar(
        title = { Text(getLocalizedString(MR.strings.averagesItem)) },
        actions = {
            IconButton(onClick = {
                component.cardList.value = !component.cardList.value
                Data.cardList = component.cardList.value
            }) {
                Icon(
                    imageVector = if (component.cardList.subscribeAsState().value) OtariumIcons.List else OtariumIcons.Cards,
                    contentDescription = null
                )
            }
        },
        windowInsets = WindowInsets(0)
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AveragesListScreen(component: AveragesComponent, subjects: List<Subject>,) {
    val grades = component.gradesList.subscribeAsState().value
    val manualGrades = component.manualGradesList.subscribeAsState().value

    val refreshState =
        rememberPullRefreshState(component.refreshState.subscribeAsState().value, { component.refreshGrades() })

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
                    grades.filter { subject.id == -1 || it.grade.subject.id == subject.id },
                    manualGrades.filter { subject.id == -1 || it.subjectId == subject.id },
                    component.cardList.subscribeAsState().value
                )
            }
        }

        PullRefreshIndicator(
            refreshing = component.refreshState.subscribeAsState().value,
            state = refreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}