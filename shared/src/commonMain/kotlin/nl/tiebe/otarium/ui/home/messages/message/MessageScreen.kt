package nl.tiebe.otarium.ui.home.messages.message

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.logic.root.home.children.messages.children.message.MessageComponent
import nl.tiebe.otarium.ui.utils.HtmlView

@Composable
internal fun MessageScreen(component: MessageComponent) {
    val scrollState = rememberScrollState()

    Column(Modifier.verticalScroll(scrollState).fillMaxSize()) {
        MessageHeader(component)

        val messageContent = component.message.subscribeAsState().value

        HtmlView(
            messageContent.content,
            maxLines = 0,
            backgroundColor = MaterialTheme.colorScheme.surface.toArgb()
        )
    }

}

