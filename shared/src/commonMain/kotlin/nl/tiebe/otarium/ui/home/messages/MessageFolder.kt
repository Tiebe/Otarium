package nl.tiebe.otarium.ui.home.messages

import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.icerock.moko.resources.compose.painterResource
import dev.tiebe.magisterapi.response.messages.MessageFolder
import nl.tiebe.otarium.MR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MessageFolderItem(component: MessagesComponent, folder: MessageFolder) {
    ListItem(
        headlineText = { Text(folder.name) },
        leadingContent = { Icon(painterResource(MR.images.folder), contentDescription = null) },
        modifier = Modifier.clickable {
            component.navigateToFolder(folder)
        }
    )
}