package dev.tiebe.otarium.ui.home.elo.children.studyguides.folder

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.tiebe.otarium.logic.root.home.children.elo.children.studyguides.children.folder.StudyGuideFolderComponent

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun StudyGuideFolderScreen(component: StudyGuideFolderComponent) {
    val scrollState = rememberScrollState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = component.refreshing.subscribeAsState().value,
        onRefresh = component::loadContent
    )

    val contentItems = component.contentItems.subscribeAsState().value

    Box(modifier = Modifier.fillMaxSize().pullRefresh(pullRefreshState)) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState)) {
            contentItems.forEach {
                StudyGuideFolderItem(component, it)
            }
        }

        PullRefreshIndicator(
            refreshing = component.refreshing.subscribeAsState().value,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}