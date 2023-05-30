package nl.tiebe.otarium.ui.home.elo.children.studyguides.listscreen

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
fun StudyGuideListScreen(component: StudyGuideListComponent) {
    val studyGuideItems = component.studyGuides.subscribeAsState().value
    val scrollState = rememberScrollState()
    val pullRefreshState = rememberPullRefreshState(component.isRefreshing.subscribeAsState().value, onRefresh = component::refreshStudyGuides)

    Box(modifier = Modifier.fillMaxSize().pullRefresh(pullRefreshState)) {
        Column(Modifier.fillMaxSize().verticalScroll(scrollState)) {
            studyGuideItems.forEach {
                StudyGuideListItem(component, it)

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