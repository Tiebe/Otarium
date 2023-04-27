package nl.tiebe.otarium.ui.home.messages.message

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.ui.utils.parseHtml

@Composable
internal fun MessageScreen(component: MessageComponent) {
    val scrollState = rememberScrollState()

    Column(Modifier.verticalScroll(scrollState)) {
        val messageContent = component.message.subscribeAsState().value

        Text(messageContent.content.parseHtml())
    }

}