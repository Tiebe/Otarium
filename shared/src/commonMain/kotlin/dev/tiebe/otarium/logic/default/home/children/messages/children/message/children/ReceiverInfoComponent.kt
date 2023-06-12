package dev.tiebe.otarium.logic.default.home.children.messages.children.message.children

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.api.messages.MessageFlow
import dev.tiebe.magisterapi.response.messages.MessageData
import dev.tiebe.otarium.Data
import dev.tiebe.otarium.logic.home.children.messages.MessagesComponent
import dev.tiebe.otarium.logic.default.componentCoroutineScope
import io.ktor.http.*
import kotlinx.coroutines.launch

class DefaultReceiverInfoComponent(
    componentContext: ComponentContext, override val messageLink: String, override val receiverType: ReceiverInfoComponent.ReceiverType,
    override val parentComponent: MessagesComponent
): ReceiverInfoComponent, ComponentContext by componentContext {
    val scope = componentCoroutineScope()

    override val message: MutableValue<MessageData> = MutableValue(MessageData(MessageData.Companion.Sender(0, MessageData.Companion.Links(MessageData.Companion.Link(""), MessageData.Companion.Link(""), MessageData.Companion.Link("")), ""), null, listOf(), null, false, false, 0, "", false, listOf(), MessageData.Companion.Links(MessageData.Companion.Link(""), MessageData.Companion.Link(""), MessageData.Companion.Link("")), 0, "", listOf(), ""))

    private fun getMessage() {
        scope.launch {
            message.value = MessageFlow.getMessageData(Url(Data.selectedAccount.tenantUrl), Data.selectedAccount.tokens.accessToken, messageLink)
        }
    }

    init {
        getMessage()
    }

}