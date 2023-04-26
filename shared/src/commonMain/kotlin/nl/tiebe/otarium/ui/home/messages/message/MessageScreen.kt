package nl.tiebe.otarium.ui.home.messages.message

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState

@Composable
internal fun MessageScreen(component: MessageComponent) {
    val messageContent = component.message.subscribeAsState().value

    Text(messageContent.content)

}