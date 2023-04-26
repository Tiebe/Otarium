package nl.tiebe.otarium.ui.home.messages.message

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.api.messages.MessageFlow
import dev.tiebe.magisterapi.response.messages.MessageData
import io.ktor.http.*
import kotlinx.coroutines.launch
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.ui.home.messages.MessagesComponent
import nl.tiebe.otarium.ui.root.componentCoroutineScope

interface MessageComponent {
    val parentComponent: MessagesComponent
    val messageLink: String

    val message: Value<MessageData>
}

class DefaultMessageComponent(
    componentContext: ComponentContext, override val messageLink: String,
    override val parentComponent: MessagesComponent
): MessageComponent, ComponentContext by componentContext {
    val scope = componentCoroutineScope()

    override val message: MutableValue<MessageData> = MutableValue(
        MessageData(
            MessageData.Companion.Sender(
                0,
                MessageData.Companion.Links(
                    MessageData.Companion.Link(""),
                    MessageData.Companion.Link(""),
                    MessageData.Companion.Link("")
                ),
                ""
            ),
            null,
            listOf(),
            null,
            false,
            false,
            0,
            "",
            false,
            listOf(),
            MessageData.Companion.Links(
                MessageData.Companion.Link(""),
                MessageData.Companion.Link(""),
                MessageData.Companion.Link("")
            ),
            0,
            "",
            listOf(),
            ""
        )
    )

    fun getMessage() {
        scope.launch {
            message.value = MessageFlow.getMessageData(Url(Data.selectedAccount.tenantUrl), Data.selectedAccount.tokens.accessToken, messageLink)
        }
    }

    init {
        getMessage()
    }

}