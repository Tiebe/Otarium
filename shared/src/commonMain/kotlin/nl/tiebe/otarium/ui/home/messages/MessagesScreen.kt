package nl.tiebe.otarium.ui.home.messages

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import dev.tiebe.magisterapi.response.messages.MessageFolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.logic.root.home.children.messages.MessagesComponent
import nl.tiebe.otarium.logic.root.home.children.messages.children.folder.FolderComponent
import nl.tiebe.otarium.ui.home.messages.composing.MessageComposeScreen
import nl.tiebe.otarium.ui.home.messages.composing.MessageComposeTopAppBar
import nl.tiebe.otarium.ui.home.messages.folder.FolderScreen
import nl.tiebe.otarium.ui.home.messages.message.MessageScreen
import nl.tiebe.otarium.ui.home.messages.message.MessageTopAppBar
import nl.tiebe.otarium.ui.home.messages.message.ReceiverTopAppBar
import nl.tiebe.otarium.ui.home.messages.message.receiver.ReceiverInfoScreen
import nl.tiebe.otarium.utils.OtariumIcons
import nl.tiebe.otarium.utils.otariumicons.Email
import nl.tiebe.otarium.utils.otariumicons.email.*
import nl.tiebe.otarium.utils.ui.getLocalizedString

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
                    text = getLocalizedString(MR.strings.messagesItem),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp)
                )

                for (folder in foldersState.value.filter { it.parentId == 0 }) {
                    val (name, icon) = GetFolderIcon(folder)

                    FolderNavigationItem(icon, name, screen, folder, scope, drawerState, component)

                    for (subFolder in foldersState.value.filter { it.parentId == folder.id }) {
                        val (subName, subIcon) = GetFolderIcon(subFolder)

                        FolderNavigationItem(subIcon, subName, screen, subFolder, scope, drawerState, component, modifier = Modifier.padding(start = 16.dp))
                    }
                }
            }
        },
        drawerState = drawerState,
        gesturesEnabled = screen.value.active.instance is MessagesComponent.Child.FolderChild || drawerState.isOpen
    ) {
        FolderContent(screen, component, drawerState)
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
    drawerState: DrawerState
) {
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            when (val instance = screen.value.active.instance) {
                is MessagesComponent.Child.FolderChild -> FolderTopAppBar(instance.component, drawerState)
                is MessagesComponent.Child.MessageChild -> MessageTopAppBar(instance.component)
                is MessagesComponent.Child.ReceiverInfoChild -> ReceiverTopAppBar(instance.component, drawerState)
                is MessagesComponent.Child.ComposeChild -> MessageComposeTopAppBar(instance.component)
            }
        },
        floatingActionButton = {
           if (screen.value.active.instance is MessagesComponent.Child.FolderChild) {
               FloatingActionButton(
                   onClick = {
                       scope.launch {
                           component.navigate(MessagesComponent.Config.Compose(listOf(), "", ""))
                       }
                   },
                   content = {
                       Icon(
                           imageVector = OtariumIcons.Email.Pencil,
                           contentDescription = "Compose new message"
                       )
                   },
                   containerColor = MaterialTheme.colorScheme.primary,
                   contentColor = MaterialTheme.colorScheme.onPrimary
               )
           }
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
                    is MessagesComponent.Child.ComposeChild -> MessageComposeScreen(instance.component)
                }
            }
        }
    }
}

@Composable
fun GetFolderIcon(folder: MessageFolder): Pair<String, @Composable () -> Unit> =
    when (folder.id) {
        1 -> getLocalizedString(MR.strings.email_inbox) to { Icon(OtariumIcons.Email.Inbox, contentDescription = "Inbox") }
        2 -> getLocalizedString(MR.strings.email_sent) to { Icon(OtariumIcons.Email.Send, contentDescription = "Sent") }
        3 -> getLocalizedString(MR.strings.email_trash) to { Icon(OtariumIcons.Email.Delete, contentDescription = "Trash") }
        else -> folder.name to { Icon(OtariumIcons.Email.Folder, contentDescription = "Folder") }
    }

//        2 -> getLocalizedString(MR.strings.email_drafts) to { Icon(OtariumIcons.Email.Pencil, contentDescription = "Drafts") }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderTopAppBar(component: FolderComponent, drawerState: DrawerState) {
    val scope = rememberCoroutineScope()

    TopAppBar(
        title = {
            SearchBar(
                query = component.searchQuery.subscribeAsState().value,
                onQueryChange = { component.setSearchQuery(it) },
                onSearch = { component.search(it) },
                active = component.searchActive.subscribeAsState().value,
                onActiveChange = { component.setSearchActive(it) },
                leadingIcon = { Icon(Icons.Default.Search, "Search") }
            ) {
                val messages = component.searchedItems.subscribeAsState().value

                messages.forEach { message ->
                    MessageItem(component.parentComponent::navigateToMessage, message)
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        },
        windowInsets = WindowInsets(0)
    )
}