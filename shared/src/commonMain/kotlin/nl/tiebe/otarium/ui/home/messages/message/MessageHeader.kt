package nl.tiebe.otarium.ui.home.messages.message

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.datetime.toLocalDateTime
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.ui.home.messages.MessagesComponent
import nl.tiebe.otarium.ui.home.messages.message.receiver.ReceiverInfoComponent
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

    Divider(modifier = Modifier.padding(bottom = 10.dp))

}