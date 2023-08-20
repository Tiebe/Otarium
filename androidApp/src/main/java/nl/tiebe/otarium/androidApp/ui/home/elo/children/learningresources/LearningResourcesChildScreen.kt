package nl.tiebe.otarium.androidApp.ui.home.elo.children.learningresources

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.logic.root.home.children.elo.children.learningresources.LearningResourcesChildComponent

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun LearningResourcesChildScreen(component: LearningResourcesChildComponent) {
    val learningResources = component.learningResources.subscribeAsState().value
    val pullRefreshState = rememberPullRefreshState(component.isRefreshing.subscribeAsState().value, component::refreshLearningResources)

    Box(modifier = Modifier.fillMaxSize().pullRefresh(pullRefreshState)) {
        val scrollState = rememberScrollState()

        Column(Modifier.verticalScroll(scrollState)) {
            learningResources.forEach {
                ListItem(
                    headlineText = { Text(it.title) },
                    modifier = Modifier.clickable { component.openLearningResource(it) }
                )

                Divider()
            }
        }

        PullRefreshIndicator(
            state = pullRefreshState,
            refreshing = component.isRefreshing.subscribeAsState().value,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }

}