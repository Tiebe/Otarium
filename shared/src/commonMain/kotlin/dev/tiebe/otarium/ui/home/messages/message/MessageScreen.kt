package dev.tiebe.otarium.ui.home.messages.message

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.tiebe.otarium.logic.root.home.children.messages.children.message.MessageComponent
import dev.tiebe.otarium.ui.utils.ClickableText
import dev.tiebe.otarium.ui.utils.parseHtml

@Composable
internal fun MessageScreen(component: MessageComponent) {
    val scrollState = rememberScrollState()

    Column(Modifier.verticalScroll(scrollState).fillMaxSize()) {
        MessageHeader(component)

        val messageContent = component.message.subscribeAsState().value
        val text = messageContent.content.parseHtml()

        ClickableText(
            text = text,
            modifier = Modifier.fillMaxSize()
        )
    }

}

