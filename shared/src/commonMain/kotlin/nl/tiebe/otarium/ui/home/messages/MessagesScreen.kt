package nl.tiebe.otarium.ui.home.messages

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.*
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.icerock.moko.resources.compose.stringResource
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.logic.root.home.children.messages.MessagesComponent
import nl.tiebe.otarium.ui.home.messages.folder.FolderScreen
import nl.tiebe.otarium.ui.home.messages.message.MessageScreen
import nl.tiebe.otarium.ui.home.messages.message.receiver.ReceiverInfoScreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalDecomposeApi::class)
@Composable
internal fun MessagesScreen(component: MessagesComponent) {
    val screen = component.childStack.subscribeAsState()
    val scope = rememberCoroutineScope()

    Children(
        screen.value,
        animation = predictiveBackAnimation(
            backHandler = component.backHandler,
            animation = stackAnimation(fade() + scale()), // Your usual animation here
            onBack = component::back,
        )
    ) { child ->
        Scaffold(
            topBar = {
                val name = when (val instance = child.instance) {
                    is MessagesComponent.Child.FolderChild -> instance.component.folder.name
                    is MessagesComponent.Child.MessageChild -> instance.component.message.subscribeAsState().value.subject
                    else -> stringResource(MR.strings.messagesItem)
                }

                TopAppBar(
                    title = { Text(name, overflow = TextOverflow.Ellipsis, maxLines = 1) },
                    navigationIcon = {
                        if (child.instance !is MessagesComponent.Child.MainChild) {
                            IconButton(onClick = component::back) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                            }
                        }
                    },
                    windowInsets = WindowInsets(0)
                )
            },
            contentWindowInsets = WindowInsets(0)
        ) {
            Surface(modifier = Modifier.fillMaxSize().padding(it)) {
                when (val instance = child.instance) {
                    is MessagesComponent.Child.FolderChild -> FolderScreen(instance.component)
                    is MessagesComponent.Child.MainChild -> MessagesFolderSelectScreen(instance.component)
                    is MessagesComponent.Child.MessageChild -> MessageScreen(instance.component)
                    is MessagesComponent.Child.ReceiverInfoChild -> ReceiverInfoScreen(instance.component)
                }
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