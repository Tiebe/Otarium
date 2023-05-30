package nl.tiebe.otarium.ui.home.messages.message

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextLayoutResult
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.ui.utils.ClickableText
import nl.tiebe.otarium.ui.utils.parseHtml
import nl.tiebe.otarium.utils.openUrl

@Composable
fun MessageScreen(component: MessageComponent) {
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

