package nl.tiebe.otarium.ui.home.grades.recentgrades

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
internal fun RecentGradesChild(component: RecentGradesChildComponent) {
    val refreshState = rememberSwipeRefreshState(component.refreshState.subscribeAsState().value)

    SwipeRefresh(state = refreshState, onRefresh = { component.refreshGrades() }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            val grades = component.grades.subscribeAsState().value

            grades.forEach {
                RecentGradeItem(grade = it)
            }
        }
    }
}
