package nl.tiebe.otarium.ui.home.messages.message

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.style.TextOverflow
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import kotlinx.coroutines.launch
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.logic.root.home.children.messages.MessagesComponent
import nl.tiebe.otarium.logic.root.home.children.messages.children.message.MessageComponent
import nl.tiebe.otarium.logic.root.home.children.messages.children.message.children.ReceiverInfoComponent
import nl.tiebe.otarium.utils.OtariumIcons
import nl.tiebe.otarium.utils.otariumicons.Email
import nl.tiebe.otarium.utils.otariumicons.email.DeleteRestore
import nl.tiebe.otarium.utils.otariumicons.email.Reply
import nl.tiebe.otarium.utils.otariumicons.email.Share
import nl.tiebe.otarium.utils.ui.getLocalizedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageTopAppBar(component: MessageComponent) {
    val scope = rememberCoroutineScope()
    val message = component.message.subscribeAsState().value

    TopAppBar(
        title = { Text(message.subject, overflow = TextOverflow.Ellipsis, maxLines = 1) },
        navigationIcon = {
            IconButton(onClick = { component.parentComponent.back() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            // trash, move to folder, reply, forward
/*            IconButton(onClick = { scope.launch { component.moveToFolder() } }) {
                Icon(OtariumIcons.Email.FolderSwap, contentDescription = "Move to folder")
            }*/
            IconButton(onClick = { scope.launch {
                component.parentComponent.navigate(
                    MessagesComponent.Config.Compose(
                        subject = "RE: ${message.subject}",
                        body = "On ${message.sender.naam} ${message.sender} wrote:\n${message.content}",
                        receivers = listOf()
                    )
                )
            } }) {
                Icon(OtariumIcons.Email.Reply, contentDescription = "Reply")
            }
            IconButton(onClick = { scope.launch {
                component.parentComponent.navigate(
                    MessagesComponent.Config.Compose(
                        subject = "FWD: ${message.subject}",
                        body = "On ${message.sender.naam} ${message.sender} wrote:\n${message.content}",
                        receivers = listOf()
                    )
                )
            } }) {
                Icon(OtariumIcons.Email.Share, contentDescription = "Forward")
            }

            IconButton(onClick = { scope.launch { component.deleteMessage() } }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
            if (message.folderId == 3) {
                IconButton(onClick = { scope.launch { component.restoreMessage() } }) {
                    Icon(OtariumIcons.Email.DeleteRestore, contentDescription = "Restore")
                }
            }
        },
        windowInsets = WindowInsets(0)
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiverTopAppBar(component: ReceiverInfoComponent, drawerState: DrawerState) {
    TopAppBar(
        title = { Text(getLocalizedString(MR.strings.receiverInfo), overflow = TextOverflow.Ellipsis, maxLines = 1) },
        navigationIcon = {
            IconButton(onClick = { component.parentComponent.back() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        windowInsets = WindowInsets(0)
    )
}