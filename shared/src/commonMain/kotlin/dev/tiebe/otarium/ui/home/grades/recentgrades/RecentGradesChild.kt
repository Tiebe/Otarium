package dev.tiebe.otarium.ui.home.grades.recentgrades

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.tiebe.otarium.logic.root.home.children.grades.children.recent.RecentGradesChildComponent

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun RecentGradesChild(component: RecentGradesChildComponent) {
    val refreshState = rememberPullRefreshState(component.refreshState.subscribeAsState().value, { component.refreshGrades() })

    Box(Modifier.pullRefresh(refreshState)) {
        val grades = component.grades.subscribeAsState().value

        val scrollState = rememberScrollState()
        val reachedEnd = derivedStateOf { scrollState.value == scrollState.maxValue }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
        ) {
            grades.forEach {
                RecentGradeItem(component = component, grade = it)
            }

            if (reachedEnd.value) {
                LaunchedEffect(Unit) {
                    if (!component.refreshState.value)
                        component.loadNextGrades()
                }
            }
        }

        PullRefreshIndicator(
            refreshing = component.refreshState.subscribeAsState().value,
            state = refreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}
