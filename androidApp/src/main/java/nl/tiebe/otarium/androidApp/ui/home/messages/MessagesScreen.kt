package nl.tiebe.otarium.androidApp.ui.home.messages

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.*
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import dev.tiebe.magisterapi.response.messages.MessageFolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.androidApp.ui.home.messages.folder.FolderScreen
import nl.tiebe.otarium.androidApp.ui.home.messages.message.MessageScreen
import nl.tiebe.otarium.androidApp.ui.home.messages.message.receiver.ReceiverInfoScreen
import nl.tiebe.otarium.logic.root.home.children.messages.MessagesComponent
import nl.tiebe.otarium.utils.OtariumIcons
import nl.tiebe.otarium.utils.otariumicons.Email
import nl.tiebe.otarium.utils.otariumicons.email.Delete
import nl.tiebe.otarium.utils.otariumicons.email.Folder
import nl.tiebe.otarium.utils.otariumicons.email.Inbox
import nl.tiebe.otarium.utils.otariumicons.email.Send

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MessagesScreen(component: MessagesComponent) {
    val screen = component.childStack.subscribeAsState()
    val scope = rememberCoroutineScope()


    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val foldersState = component.folders.subscribeAsState()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(windowInsets = WindowInsets(0)) {
                Text(
                    text = stringResource(MR.strings.messagesItem.resourceId),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp)
                )


                for (folder in foldersState.value.filter { it.parentId == 0 }) {
                    val (name, icon) = getFolderIcon(folder)

                    FolderNavigationItem(icon, name, screen, folder, scope, drawerState, component)

                    for (subFolder in foldersState.value.filter { it.parentId == folder.id }) {
                        val (subName, subIcon) = getFolderIcon(subFolder)

                        FolderNavigationItem(subIcon, subName, screen, subFolder, scope, drawerState, component, modifier = Modifier.padding(start = 16.dp))
                    }


                }
            }

        },
        drawerState = drawerState,
        gesturesEnabled = screen.value.active.instance is MessagesComponent.Child.FolderChild || drawerState.isOpen
    ) {
        FolderContent(screen, component, scope, drawerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FolderNavigationItem(
    icon: @Composable () -> Unit,
    name: String,
    screen: State<ChildStack<MessagesComponent.Config, MessagesComponent.Child>>,
    folder: MessageFolder,
    scope: CoroutineScope,
    drawerState: DrawerState,
    component: MessagesComponent,
    modifier: Modifier = Modifier
) {
    NavigationDrawerItem(
        icon = icon,
        label = { Text(name) },
        selected = screen.value.active.configuration is MessagesComponent.Config.Folder && (screen.value.active.configuration as MessagesComponent.Config.Folder).folderId == folder.id,
        onClick = {
            scope.launch {
                drawerState.close()
                component.navigateToFolder(folder)
            }
        },
        badge = {
            if (folder.unreadCount > 0) {
                Badge(
                    content = { Text(folder.unreadCount.toString()) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding).then(modifier)
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalDecomposeApi::class)
@Composable
private fun FolderContent(
    screen: State<ChildStack<MessagesComponent.Config, MessagesComponent.Child>>,
    component: MessagesComponent,
    scope: CoroutineScope,
    drawerState: DrawerState
) {
    Scaffold(
        topBar = {
            val instance = screen.value.active.instance

            val name = when (instance) {
                is MessagesComponent.Child.FolderChild -> getFolderIcon(instance.component.folder).first
                is MessagesComponent.Child.MessageChild -> instance.component.message.subscribeAsState().value.subject
                is MessagesComponent.Child.ReceiverInfoChild -> stringResource(MR.strings.receiverInfo.resourceId)
            }

            TopAppBar(
                title = { Text(name, overflow = TextOverflow.Ellipsis, maxLines = 1) },
                navigationIcon = {
                    if (instance !is MessagesComponent.Child.FolderChild) {
                        IconButton(onClick = component::back) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    } else {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                },
                windowInsets = WindowInsets(0)
            )
        },
        contentWindowInsets = WindowInsets(0)
    ) {
        Children(
            screen.value,
            animation = predictiveBackAnimation(
                backHandler = component.backHandler,
                animation = stackAnimation(fade() + scale()), // Your usual animation here
                onBack = component::back,
            )
        ) { child ->
            Surface(modifier = Modifier.fillMaxSize().padding(it)) {
                when (val instance = child.instance) {
                    is MessagesComponent.Child.FolderChild -> FolderScreen(instance.component)
                    is MessagesComponent.Child.MessageChild -> MessageScreen(instance.component)
                    is MessagesComponent.Child.ReceiverInfoChild -> ReceiverInfoScreen(instance.component)
                }
            }
        }
    }
}

@Composable
fun getFolderIcon(folder: MessageFolder): Pair<String, @Composable () -> Unit> =
    when (folder.id) {
        1 -> stringResource(MR.strings.email_inbox.resourceId) to { Icon(OtariumIcons.Email.Inbox, contentDescription = "Inbox") }
        2 -> stringResource(MR.strings.email_sent.resourceId) to { Icon(OtariumIcons.Email.Send, contentDescription = "Sent") }
        3 -> stringResource(MR.strings.email_trash.resourceId) to { Icon(OtariumIcons.Email.Delete, contentDescription = "Trash") }
        else -> folder.name to { Icon(OtariumIcons.Email.Folder, contentDescription = "Folder") }
    }

//        2 -> stringResource(MR.strings.email_drafts.resourceId) to { Icon(OtariumIcons.Email.Pencil, contentDescription = "Drafts") }