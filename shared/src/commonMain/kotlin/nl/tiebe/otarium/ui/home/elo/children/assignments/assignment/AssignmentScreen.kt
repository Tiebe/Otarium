package nl.tiebe.otarium.ui.home.elo.children.assignments.assignment

import androidx.compose.foundation.layout.Box
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun AssignmentScreen(component: AssignmentScreenComponent) {
    val assignment = component.assignment.subscribeAsState().value

    if (assignment.id == 0) return

    val pullRefreshState = rememberPullRefreshState(refreshing = component.isRefreshing.subscribeAsState().value, onRefresh = { component.refreshAssignment() })

    Box(Modifier.pullRefresh(pullRefreshState)) {
        DropdownMenu()


        PullRefreshIndicator(
            state = pullRefreshState,
            refreshing = component.isRefreshing.subscribeAsState().value,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }

}