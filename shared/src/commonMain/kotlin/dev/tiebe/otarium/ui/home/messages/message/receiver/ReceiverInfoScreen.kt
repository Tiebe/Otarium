package dev.tiebe.otarium.ui.home.messages.message.receiver

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.tiebe.magisterapi.response.messages.MessageData

@Composable
internal fun ReceiverInfoScreen(component: ReceiverInfoComponent) {
    val messageData = component.message.subscribeAsState().value

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        when (component.receiverType) {
            ReceiverInfoComponent.ReceiverType.NORMAL -> {
                messageData.receivers.forEach {
                    item {
                        ReceiverInfoItem(it)
                        Divider()
                    }
                }
            }
            ReceiverInfoComponent.ReceiverType.CC -> {
                messageData.ccReceivers.forEach {
                    item {
                        ReceiverInfoItem(it)
                        Divider()
                    }
                }
            }
            ReceiverInfoComponent.ReceiverType.BCC -> {
                messageData.bccReceivers.forEach {
                    item {
                        ReceiverInfoItem(it)
                        Divider()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReceiverInfoItem(receiver: MessageData.Companion.Receiver) {
    ListItem(
        headlineText = { Text(text = receiver.name) },
    )
}