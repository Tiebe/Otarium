package nl.tiebe.otarium.ui.home.messages.message

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import nl.tiebe.otarium.logic.root.home.children.messages.children.message.MessageComponent

@Composable
internal fun MessageScreen(component: MessageComponent) {
    val scrollState = rememberScrollState()

    Column(Modifier.verticalScroll(scrollState).fillMaxSize()) {
        MessageHeader(component)

        val messageContent = component.message.subscribeAsState().value
        val state = rememberRichTextState()

        LaunchedEffect(messageContent) {
            state.setHtml(messageContent.content)
        }

        RichText(
            state,
            modifier = Modifier.fillMaxSize()
        )
    }

}

