package nl.tiebe.otarium.ui.home.elo.children.studyguides.listscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun StudyGuideListScreen(component: StudyGuideListComponent) {
    val studyGuideItems = component.studyGuides.subscribeAsState().value
    val scrollState = rememberScrollState()
    val pullRefreshState = rememberPullRefreshState(component.isRefreshing.subscribeAsState().value, onRefresh = component::refreshStudyGuides)

    Column(Modifier.fillMaxSize().pullRefresh(pullRefreshState).verticalScroll(scrollState)) {
        studyGuideItems.forEach {
            StudyGuideListItem(component, it)

            Divider()
        }
    }
}