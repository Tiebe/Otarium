package nl.tiebe.otarium.ui.home.messages

import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.icerock.moko.resources.compose.painterResource
import dev.tiebe.magisterapi.response.messages.Message
import dev.tiebe.magisterapi.response.messages.MessageFolder
import nl.tiebe.otarium.MR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MessageFolderItem(navigateToFolder: (MessageFolder) -> Unit, folder: MessageFolder) {
    ListItem(
        headlineText = { Text(folder.name) },
        leadingContent = { Icon(painterResource(MR.images.folder), contentDescription = null) },
        modifier = Modifier.clickable {
            navigateToFolder(folder)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MessageItem(navigateToMessage: (Message) -> Unit, message: Message) {
    val icon = if (message.hasBeenRead)
        if (message.hasPriority) MR.images.email_alert_open else MR.images.email_open
    else if (message.hasPriority) MR.images.email_alert else MR.images.email_filled

    ListItem(
        headlineText = { Text(message.subject) },
        supportingText = { Text(message.sender?.name ?: message.receivers?.joinToString { it.name } ?: "") },
        leadingContent = { Icon(painterResource(icon), contentDescription = null) },
        modifier = Modifier.clickable {
            navigateToMessage(message)
        }
    )
}
