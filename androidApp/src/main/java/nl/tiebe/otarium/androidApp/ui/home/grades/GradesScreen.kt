package nl.tiebe.otarium.androidApp.ui.home.grades

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.logic.root.home.children.grades.GradesComponent

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun GradesScreen(component: GradesComponent) {
    val refreshState = rememberPullRefreshState(component.refreshState.subscribeAsState().value, { component.refreshGrades() })

    Box(Modifier.pullRefresh(refreshState)) {
        val grades = component.grades.subscribeAsState().value

        val scrollState = rememberScrollState()
        val reachedEnd by remember { derivedStateOf { scrollState.value == scrollState.maxValue } }

        LaunchedEffect(reachedEnd) {
            if (reachedEnd && !component.refreshState.value)
                component.loadNextGrades()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
        ) {
            grades.forEach {
                GradeItem(component = component, grade = it)
            }
        }

        PullRefreshIndicator(
            refreshing = component.refreshState.subscribeAsState().value,
            state = refreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}
