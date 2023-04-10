package nl.tiebe.otarium.ui.home.grades.recentgrades

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(grades) {
                RecentGradeItem(component = component, grade = it)
            }

            item {
                LaunchedEffect(true) {
                    if (!refreshState.isRefreshing)
                        component.loadNextGrades()
                }
            }
        }
    }
}
