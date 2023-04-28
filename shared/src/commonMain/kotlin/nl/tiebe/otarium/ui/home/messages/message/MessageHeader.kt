package nl.tiebe.otarium.ui.home.messages.message

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.datetime.toLocalDateTime
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.ui.home.messages.MessagesComponent
import nl.tiebe.otarium.ui.home.messages.message.receiver.ReceiverInfoComponent
import nl.tiebe.otarium.ui.utils.drawLinearIndicator
import nl.tiebe.otarium.ui.utils.drawLinearIndicatorTrack
import nl.tiebe.otarium.utils.toFormattedString

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
                                painterResource(if (attachment.status == "available") MR.images.attachment else MR.images.attachment_off),
                                contentDescription = "Attachment"
                            )
                            Text(text = attachment.name, modifier = Modifier.padding(start = 10.dp))
                        }

                        val progress =
                            component.attachmentDownloadProgress.subscribeAsState().value[attachment.id] ?: 0f

                        if (progress != 0f && progress != 1f && !progress.isNaN()) {
                            val color = MaterialTheme.colorScheme.primary
                            val trackColor = MaterialTheme.colorScheme.surfaceVariant


                            Canvas(
                                Modifier
                                    .progressSemantics(progress)
                                    .matchParentSize()
                                    .align(Alignment.BottomStart)
                            ) {
                                val strokeWidth = 5.dp
                                drawLinearIndicatorTrack(trackColor, strokeWidth.toPx())
                                drawLinearIndicator(0f, progress, color, strokeWidth.toPx())
                            }


                        }
                    }
                }
            }

        }
    }


    Divider(modifier = Modifier.padding(bottom = 10.dp))

}