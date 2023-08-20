package nl.tiebe.otarium.androidApp.ui.home.messages.folder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.androidApp.ui.home.messages.MessageItem
import nl.tiebe.otarium.logic.root.home.children.messages.children.folder.FolderComponent

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun FolderScreen(component: FolderComponent) {
    val refreshState = rememberPullRefreshState(refreshing = component.refreshState.subscribeAsState().value, onRefresh = component::refresh)

    Box(
        modifier = Modifier.pullRefresh(refreshState)
    ) {
        val scrollState = component.scrollState
        val reachedEnd by remember { derivedStateOf { scrollState.value == scrollState.maxValue } }

        if (reachedEnd) {
            LaunchedEffect(Unit) {
                if (!component.refreshState.value)
                    component.loadNewMessages()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
        ) {
            val messages = component.messages.subscribeAsState().value

            messages.forEach { message ->
                MessageItem(component.parentComponent::navigateToMessage, message)
            }

        }

        PullRefreshIndicator(refreshing = component.refreshState.subscribeAsState().value, state = refreshState, modifier = Modifier.align(Alignment.TopCenter))
    }

}