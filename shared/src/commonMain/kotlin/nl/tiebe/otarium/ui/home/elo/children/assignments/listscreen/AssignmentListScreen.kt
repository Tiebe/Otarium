package nl.tiebe.otarium.ui.home.elo.children.assignments.listscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun AssignmentListScreen(component: AssignmentListComponent) {
    val assignments = component.assignments.subscribeAsState().value

    val scrollState = rememberScrollState()
    val pullRefreshState = rememberPullRefreshState(component.isRefreshing.subscribeAsState().value, onRefresh = component::refreshAssignments)

    Box(modifier = Modifier.fillMaxSize().pullRefresh(pullRefreshState)) {
        Column(Modifier.fillMaxSize().verticalScroll(scrollState)) {
            assignments.forEach {
                AssignmentListItem(component, it)

                Divider()
            }
        }

        PullRefreshIndicator(
            refreshing = component.isRefreshing.subscribeAsState().value,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}