package dev.tiebe.otarium.ui.home.messages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.pop
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.launch
import dev.tiebe.otarium.MR
import dev.tiebe.otarium.ui.home.messages.folder.FolderScreen
import dev.tiebe.otarium.ui.home.messages.message.MessageScreen
import dev.tiebe.otarium.ui.home.messages.message.receiver.ReceiverInfoScreen

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun MessagesScreen(component: MessagesComponent) {
    val screen = component.childStack.subscribeAsState()
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.padding(start = 5.dp, end = 5.dp)) {
        MessageScreenChild(component, screen, screen.value.items[0], false)

        for (item in screen.value.items.subList(1, screen.value.items.size)) {
            val state = rememberDismissState()

            component.onBack.value = {
                scope.launch {
                    state.animateTo(DismissValue.DismissedToEnd)
                }
            }

            //pop on finish
            if (state.isDismissed(DismissDirection.StartToEnd)) {
                component.navigation.pop()
            }

            SwipeToDismiss(
                state = state,
                background = {
                },
                directions = setOf(DismissDirection.StartToEnd)
            ) {
                MessageScreenChild(component, screen, item)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MessageScreenChild(
    component: MessagesComponent,
    screen: State<ChildStack<MessagesComponent.Config, MessagesComponent.Child>>,
    item: Child.Created<MessagesComponent.Config, MessagesComponent.Child>,
    poppable: Boolean = screen.value.backStack.isNotEmpty()
) {
    val name = when (val child = item.instance) {
        is MessagesComponent.Child.FolderChild -> child.component.folder.name
        is MessagesComponent.Child.MessageChild -> child.component.message.subscribeAsState().value.subject
        else -> stringResource(MR.strings.messagesItem)
    }
    Column {
        TopAppBar(
            title = { Text(name, overflow = TextOverflow.Ellipsis, maxLines = 1) },
            navigationIcon = {
                if (poppable) {
                    IconButton(onClick = { component.onBack.value() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            },
            windowInsets = WindowInsets(0.dp)
        )

        Surface(modifier = Modifier.fillMaxSize()) {
            when (val child = item.instance) {
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
    val refreshState = rememberPullRefreshState(
        refreshing = component.refreshState.subscribeAsState().value,
        onRefresh = component::getFolders
    )

    Box(modifier = Modifier.fillMaxSize().pullRefresh(refreshState)) {
        val folders = foldersState.value.filter { it.parentId == 0 }

        Column(modifier = Modifier.fillMaxSize()) {
            folders.forEach { folder ->
                MessageFolderItem(component::navigateToFolder, folder)
                Divider()
            }
        }

        PullRefreshIndicator(
            component.refreshState.subscribeAsState().value,
            refreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}