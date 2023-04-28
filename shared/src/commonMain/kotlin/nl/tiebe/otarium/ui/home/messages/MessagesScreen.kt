package nl.tiebe.otarium.ui.home.messages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.router.stack.pop
import dev.icerock.moko.resources.compose.stringResource
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.ui.home.messages.folder.FolderScreen
import nl.tiebe.otarium.ui.home.messages.message.MessageScreen
import nl.tiebe.otarium.ui.home.messages.message.receiver.ReceiverInfoScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MessagesScreen(component: MessagesComponent) {
    Column {
        val screen = component.childStack.subscribeAsState()

        val name = when (val child = screen.value.active.instance) {
            is MessagesComponent.Child.FolderChild -> child.component.folder.name
            is MessagesComponent.Child.MessageChild -> child.component.message.subscribeAsState().value.subject
            else -> stringResource(MR.strings.messagesItem)
        }

        TopAppBar(
            title = { Text(name, overflow = TextOverflow.Ellipsis, maxLines = 1) },
            navigationIcon = {
                if (screen.value.backStack.isNotEmpty()) {
                    IconButton(onClick = { component.navigation.pop() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            }
        )

        Box(modifier = Modifier.padding(start = 5.dp, end = 5.dp)) {
            when (val child = screen.value.active.instance) {
                is MessagesComponent.Child.FolderChild -> FolderScreen(child.component)
                is MessagesComponent.Child.MainChild -> MessagesFolderSelectScreen(child.component)
                is MessagesComponent.Child.MessageChild -> MessageScreen(child.component)
                is MessagesComponent.Child.ReceiverInfoChild -> ReceiverInfoScreen(child.component)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun MessagesFolderSelectScreen(component: MessagesComponent) {
    val foldersState = component.folders.subscribeAsState()
    val refreshState = rememberPullRefreshState(refreshing = component.refreshState.subscribeAsState().value, onRefresh = component::getFolders)

    Box(modifier = Modifier.fillMaxSize().pullRefresh(refreshState)) {
        val folders = foldersState.value.filter { it.parentId == 0 }

        Column(modifier = Modifier.fillMaxSize()) {
            folders.forEach { folder ->
                MessageFolderItem(component::navigateToFolder, folder)
                Divider()
            }
        }

        PullRefreshIndicator(component.refreshState.subscribeAsState().value, refreshState, modifier = Modifier.align(Alignment.TopCenter))
    }
}