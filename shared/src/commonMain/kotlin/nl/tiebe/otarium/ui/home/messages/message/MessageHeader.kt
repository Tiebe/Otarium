package nl.tiebe.otarium.ui.home.messages.message

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.icerock.moko.resources.compose.stringResource
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.logic.root.home.children.messages.MessagesComponent
import nl.tiebe.otarium.logic.root.home.children.messages.children.message.MessageComponent
import nl.tiebe.otarium.logic.root.home.children.messages.children.message.children.ReceiverInfoComponent
import nl.tiebe.otarium.ui.utils.DownloadIndicator
import nl.tiebe.otarium.utils.OtariumIcons
import nl.tiebe.otarium.utils.otariumicons.Email
import nl.tiebe.otarium.utils.otariumicons.email.Attachment
import nl.tiebe.otarium.utils.otariumicons.email.AttachmentOff
import nl.tiebe.otarium.utils.toFormattedString
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MessageHeader(component: MessageComponent) {
    val message = component.message.subscribeAsState().value

    if (message.id == 0) return

    ListItem(
        headlineText = { Text(text = message.subject) },
        overlineText = { Text(text = stringResource(MR.strings.message_subject)) },
    )

    Divider()

    ListItem(
        headlineText = { Text(text = message.sender.naam) },
        overlineText = { Text(text = stringResource(MR.strings.message_sender)) },
    )

    Divider()

    ListItem(
        headlineText = { Text(text = message.receivers.joinToString(limit = 10) { it.name }) },
        overlineText = { Text(text = stringResource(MR.strings.message_receivers)) },
        modifier = Modifier.clickable { component.parentComponent.navigate(MessagesComponent.Config.ReceiverInfo(component.messageLink, ReceiverInfoComponent.ReceiverType.NORMAL)) }
    )

    Divider()

    if (message.ccReceivers.isNotEmpty()) {
        ListItem(
            headlineText = { Text(text = message.ccReceivers.joinToString(limit = 10) { it.name }) },
            overlineText = { Text(text = stringResource(MR.strings.message_cc_receivers)) },
            modifier = Modifier.clickable { component.parentComponent.navigate(MessagesComponent.Config.ReceiverInfo(component.messageLink, ReceiverInfoComponent.ReceiverType.CC)) }
        )

        Divider()
    }

    if (message.bccReceivers.isNotEmpty()) {
        ListItem(
            headlineText = { Text(text = message.bccReceivers.joinToString(limit = 10) { it.name }) },
            overlineText = { Text(text = stringResource(MR.strings.message_bcc_receivers)) },
            modifier = Modifier.clickable { component.parentComponent.navigate(MessagesComponent.Config.ReceiverInfo(component.messageLink, ReceiverInfoComponent.ReceiverType.BCC)) }
        )

        Divider()
    }

    ListItem(
        headlineText = { Text(text = message.sentOn.substring(0, 26).toLocalDateTime().toFormattedString()) },
        overlineText = { Text(text = stringResource(MR.strings.message_date)) },
    )

    val attachments = component.attachments.subscribeAsState().value

    if (attachments.isNotEmpty()) {
        Divider()

        val scrollState = rememberScrollState()

        Row(modifier = Modifier.fillMaxWidth().horizontalScroll(scrollState)) {
            for (attachment in attachments) {
                ElevatedCard(onClick = { component.downloadAttachment(attachment) }, modifier = Modifier.height(70.dp).padding(10.dp)) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Row(
                            modifier = Modifier.fillMaxSize().padding(10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                if (attachment.status == "available") OtariumIcons.Email.Attachment else OtariumIcons.Email.AttachmentOff,
                                contentDescription = "Attachment"
                            )
                            Text(text = attachment.name, modifier = Modifier.padding(start = 10.dp))
                        }

                        DownloadIndicator(component.attachmentDownloadProgress.subscribeAsState().value[attachment.id] ?: 0f)
                    }
                }
            }

        }
    }


    Divider(modifier = Modifier.padding(bottom = 10.dp))

}