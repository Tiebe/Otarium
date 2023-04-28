package nl.tiebe.otarium.ui.home.messages.message

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.api.messages.MessageFlow
import dev.tiebe.magisterapi.response.messages.Attachment
import dev.tiebe.magisterapi.response.messages.MessageData
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.launch
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.ui.home.messages.MessagesComponent
import nl.tiebe.otarium.ui.root.componentCoroutineScope
import nl.tiebe.otarium.utils.client

interface MessageComponent {
    val parentComponent: MessagesComponent
    val messageLink: String

    val message: Value<MessageData>
    val attachments: Value<List<Attachment>>
    fun downloadFile(attachment: Attachment)
}

class DefaultMessageComponent(
    componentContext: ComponentContext, override val messageLink: String,
    override val parentComponent: MessagesComponent
): MessageComponent, ComponentContext by componentContext {
    val scope = componentCoroutineScope()

    override val message: MutableValue<MessageData> = MutableValue(MessageData(MessageData.Companion.Sender(0, MessageData.Companion.Links(MessageData.Companion.Link(""), MessageData.Companion.Link(""), MessageData.Companion.Link("")), ""), null, listOf(), null, false, false, 0, "", false, listOf(), MessageData.Companion.Links(MessageData.Companion.Link(""), MessageData.Companion.Link(""), MessageData.Companion.Link("")), 0, "", listOf(), ""))
    override val attachments: MutableValue<List<Attachment>> = MutableValue(listOf())

    private fun getMessage() {
        scope.launch {
            message.value = MessageFlow.getMessageData(Url(Data.selectedAccount.tenantUrl), Data.selectedAccount.tokens.accessToken, messageLink)

            if (message.value.hasAttachments) {
                attachments.value = MessageFlow.getAttachmentList(Url(Data.selectedAccount.tenantUrl), Data.selectedAccount.tokens.accessToken, message.value.links.attachments!!.href)
            }
        }
    }

    @OptIn(InternalAPI::class)
    override fun downloadFile(attachment: Attachment) {
        scope.launch {
            val downloadLink = MessageFlow.getDownloadLink(Url(Data.selectedAccount.tenantUrl), Data.selectedAccount.tokens.accessToken, attachment.links.downloadLink.href)

            val response = client.get(downloadLink) {
                onDownload { bytesSentTotal, contentLength ->
                    //todo: show progress
                    println("Downloaded $bytesSentTotal of $contentLength")
                }
            }.content

            println(response)
            //response.copyAndClose(getDownloadFileLocation(attachment.name))
            //openFileFromCache(attachment.name)
        }
    }

    private val markAsRead: (MessageData) -> Unit = {
        if (it.id != 0) {
            scope.launch {
                MessageFlow.markMessageAsRead(
                    Url(Data.selectedAccount.tenantUrl),
                    Data.selectedAccount.tokens.accessToken,
                    it.id,
                    true
                )

                unsubscribe()
            }
        }
    }

    private val unsubscribe = {
        message.unsubscribe(markAsRead)
    }

    init {
        message.subscribe(markAsRead)

        getMessage()
    }

}