package nl.tiebe.otarium.androidApp.ui.home.messages

import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.tiebe.magisterapi.response.messages.Message
import dev.tiebe.magisterapi.response.messages.MessageFolder
import nl.tiebe.otarium.androidApp.ui.theme.red
import nl.tiebe.otarium.utils.OtariumIcons
import nl.tiebe.otarium.utils.otariumicons.Bottombar
import nl.tiebe.otarium.utils.otariumicons.Email
import nl.tiebe.otarium.utils.otariumicons.Folder
import nl.tiebe.otarium.utils.otariumicons.bottombar.EmailFilled
import nl.tiebe.otarium.utils.otariumicons.email.Attachment
import nl.tiebe.otarium.utils.otariumicons.email.EmailAlert
import nl.tiebe.otarium.utils.otariumicons.email.EmailAlertOpen
import nl.tiebe.otarium.utils.otariumicons.email.EmailOpen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MessageFolderItem(navigateToFolder: (MessageFolder) -> Unit, folder: MessageFolder) {
    ListItem(
        headlineText = { Text(folder.name) },
        leadingContent = { Icon(OtariumIcons.Folder, contentDescription = null) },
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
        if (message.hasPriority) OtariumIcons.Email.EmailAlertOpen else OtariumIcons.Email.EmailOpen
    else if (message.hasPriority) OtariumIcons.Email.EmailAlert else OtariumIcons.Bottombar.EmailFilled

    ListItem(
        headlineText = { Text(message.subject) },
        supportingText = { Text(message.sender?.name ?: message.receivers?.joinToString { it.name } ?: "") },
        leadingContent = { Icon(icon, contentDescription = null, tint = if (message.hasPriority) red else LocalContentColor.current) },
        trailingContent = { if (message.hasAttachments) Icon(OtariumIcons.Email.Attachment, contentDescription = null) },
        modifier = Modifier.clickable {
            navigateToMessage(message)
        }
    )
}
