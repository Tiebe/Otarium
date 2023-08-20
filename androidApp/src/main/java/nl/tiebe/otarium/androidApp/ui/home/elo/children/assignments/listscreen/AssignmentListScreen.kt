package nl.tiebe.otarium.androidApp.ui.home.elo.children.assignments.listscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Divider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.logic.root.home.children.elo.children.assignments.children.list.AssignmentListComponent

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun AssignmentListScreen(component: AssignmentListComponent) {
    val assignments = component.assignments.subscribeAsState().value

    val scrollState = rememberScrollState()
    val pullRefreshState = rememberPullRefreshState(component.isRefreshing.subscribeAsState().value, onRefresh = component::refreshAssignments)

    Box(modifier = Modifier.fillMaxSize().pullRefresh(pullRefreshState)) {
        Column(Modifier.fillMaxSize()) {
            val selectedTab = component.selectedTab.subscribeAsState().value

            TabRow(selectedTab) {
                Tab(selected = selectedTab == 0, onClick = { component.selectedTab.value = 0 }, modifier = Modifier.height(53.dp)) {
                    Text(stringResource(MR.strings.assignment_all.resourceId), style = TextStyle(fontSize = 14.sp))
                }

                Tab(selected = selectedTab == 1, onClick = { component.selectedTab.value = 1 }, modifier = Modifier.height(53.dp)) {
                    Text(stringResource(MR.strings.assignment_open.resourceId), style = TextStyle(fontSize = 14.sp))
                }

                Tab(selected = selectedTab == 2, onClick = { component.selectedTab.value = 2 }, modifier = Modifier.height(53.dp)) {
                    Text(stringResource(MR.strings.assignment_submitted.resourceId), style = TextStyle(fontSize = 14.sp))
                }

                Tab(selected = selectedTab == 3, onClick = { component.selectedTab.value = 3 }, modifier = Modifier.height(53.dp)) {
                    Text(stringResource(MR.strings.assignment_graded.resourceId), style = TextStyle(fontSize = 14.sp))
                }

                Tab(selected = selectedTab == 4, onClick = { component.selectedTab.value = 4 }, modifier = Modifier.height(53.dp)) {
                    Text(stringResource(MR.strings.assignment_closed.resourceId), style = TextStyle(fontSize = 14.sp))
                }
            }

            Column(Modifier.fillMaxSize().verticalScroll(scrollState)) {
                assignments.filter {
                    when (selectedTab) {
                        0 -> true
                        1 -> !it.closed
                        2 -> !it.submitAgain
                        3 -> it.gradedOn != null && !it.closed
                        4 -> it.closed
                        else -> false
                    }
                }.forEach {
                    AssignmentListItem(component, it)

                    Divider()
                }
            }
        }

        PullRefreshIndicator(
            refreshing = component.isRefreshing.subscribeAsState().value,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}
