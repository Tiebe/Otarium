package nl.tiebe.otarium.ui.home.messages

import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.tiebe.magisterapi.response.messages.Message
import dev.tiebe.magisterapi.response.messages.MessageFolder
import nl.tiebe.otarium.utils.icons.Bottombar
import nl.tiebe.otarium.utils.icons.Email
import nl.tiebe.otarium.utils.icons.Folder
import nl.tiebe.otarium.utils.icons.Icons
import nl.tiebe.otarium.utils.icons.bottombar.EmailFilled
import nl.tiebe.otarium.utils.icons.email.Attachment
import nl.tiebe.otarium.utils.icons.email.EmailAlert
import nl.tiebe.otarium.utils.icons.email.EmailAlertOpen
import nl.tiebe.otarium.utils.icons.email.EmailOpen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MessageFolderItem(navigateToFolder: (MessageFolder) -> Unit, folder: MessageFolder) {
    ListItem(
        headlineText = { Text(folder.name) },
        leadingContent = { Icon(Icons.Folder, contentDescription = null) },
        trailingContent = { Text(folder.unreadCount.toString()) },
        modifier = Modifier.clickable {
            navigateToFolder(folder)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MessageItem(navigateToMessage: (Message) -> Unit, message: Message) {
    val icon = if (message.hasBeenRead)
        if (message.hasPriority) Icons.Email.EmailAlertOpen else Icons.Email.EmailOpen
    else if (message.hasPriority) Icons.Email.EmailAlert else Icons.Bottombar.EmailFilled

    ListItem(
        headlineText = { Text(message.subject) },
        supportingText = { Text(message.sender?.name ?: message.receivers?.joinToString { it.name } ?: "") },
        leadingContent = { Icon(icon, contentDescription = null) },
        trailingContent = { if (message.hasAttachments) Icon(Icons.Email.Attachment, contentDescription = null) },
        modifier = Modifier.clickable {
            navigateToMessage(message)
        }
    )
}
