package nl.tiebe.otarium.ui.home.messages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.router.stack.pop
import com.google.accompanist.swiperefresh.SwipeRefresh
import nl.tiebe.otarium.ui.home.messages.folder.FolderScreen
import nl.tiebe.otarium.ui.home.messages.message.MessageScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MessagesScreen(component: MessagesComponent) {
    Column {
        val screen = component.childStack.subscribeAsState()

        if (screen.value.active.instance !is MessagesComponent.Child.MainChild) {
            val name = when (val child = screen.value.active.instance) {
                is MessagesComponent.Child.FolderChild -> child.component.folder.name
                is MessagesComponent.Child.MessageChild -> child.component.message.subscribeAsState().value.subject
                else -> ""
            }

            TopAppBar(
                title = { Text(name) },
                navigationIcon = {
                    IconButton(onClick = { component.navigation.pop() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }

        Box(modifier = Modifier.padding(start = 5.dp, end = 5.dp)) {
            when (val child = screen.value.active.instance) {
                is MessagesComponent.Child.FolderChild -> FolderScreen(child.component)
                is MessagesComponent.Child.MainChild -> MessagesFolderSelectScreen(child.component)
                is MessagesComponent.Child.MessageChild -> MessageScreen(child.component)
            }
        }
    }
}

@Composable
internal fun MessagesFolderSelectScreen(component: MessagesComponent) {
    val foldersState = component.folders.subscribeAsState()

    SwipeRefresh(
        state = component.refreshState,
        onRefresh = component::getFolders
    ) {
        val folders = foldersState.value.filter { it.parentId == 0 }

        Column(modifier = Modifier.fillMaxSize()) {
            folders.forEach { folder ->
                MessageFolderItem(component::navigateToFolder, folder)
                Divider()
            }
        }
    }
}