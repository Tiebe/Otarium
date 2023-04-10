package nl.tiebe.otarium.ui.home.grades.recentgrades

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun RecentGradesChild(component: RecentGradesChildComponent) {
    val refreshState = rememberSwipeRefreshState(component.refreshState.subscribeAsState().value)

    SwipeRefresh(state = refreshState, onRefresh = { component.refreshGrades() }) {
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
                    if (!refreshState.isRefreshing)
                        component.loadNextGrades()
                }
            }
        }
    }
}
