package nl.tiebe.otarium.ui.home.messages.folder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.google.accompanist.swiperefresh.SwipeRefresh
import nl.tiebe.otarium.ui.home.messages.MessageFolderItem
import nl.tiebe.otarium.ui.home.messages.MessageItem

@Composable
internal fun FolderScreen(component: FolderComponent) {
    SwipeRefresh(
        state = component.refreshState,
        onRefresh = component::refresh
    ) {
        val scrollState = rememberScrollState()
        val reachedEnd = derivedStateOf { scrollState.value == scrollState.maxValue }

        if (reachedEnd.value) {
            LaunchedEffect(Unit) {
                if (!component.refreshState.isRefreshing)
                    component.loadNewMessages()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
        ) {
            val subFolders = component.subFolders.subscribeAsState().value
            val messages = component.messages.subscribeAsState().value

            subFolders.forEach { folder ->
                MessageFolderItem(component.parentComponent::navigateToFolder, folder)
            }

            messages.forEach { message ->
                MessageItem(component.parentComponent::navigateToMessage, message)
            }

        }
    }

}