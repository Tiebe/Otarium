package dev.tiebe.otarium.logic.default.home.children.messages.children.message

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import dev.tiebe.magisterapi.api.messages.MessageFlow
import dev.tiebe.magisterapi.response.messages.Attachment
import dev.tiebe.magisterapi.response.messages.MessageData
import dev.tiebe.otarium.Data
import dev.tiebe.otarium.logic.default.componentCoroutineScope
import dev.tiebe.otarium.logic.root.home.children.messages.MessagesComponent
import dev.tiebe.otarium.logic.root.home.children.messages.children.message.MessageComponent
import dev.tiebe.otarium.utils.openFileFromCache
import dev.tiebe.otarium.utils.requestGET
import dev.tiebe.otarium.utils.writeFile
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.launch

class DefaultMessageComponent(
    componentContext: ComponentContext, override val messageLink: String,
    override val parentComponent: MessagesComponent
): MessageComponent, ComponentContext by componentContext {
    val scope = componentCoroutineScope()

    @Suppress("DuplicatedCode")
    override val message: MutableValue<MessageData> = MutableValue(MessageData(MessageData.Companion.Sender(0, MessageData.Companion.Links(MessageData.Companion.Link(""), MessageData.Companion.Link(""), MessageData.Companion.Link("")), ""), null, listOf(), null, false, false, 0, "", false, listOf(), MessageData.Companion.Links(MessageData.Companion.Link(""), MessageData.Companion.Link(""), MessageData.Companion.Link("")), 0, "", listOf(), ""))
    override val attachments: MutableValue<List<Attachment>> = MutableValue(listOf())
    override val attachmentDownloadProgress: MutableValue<Map<Int, Float>> = MutableValue(mapOf())

    private fun getMessage() {
        scope.launch {
            message.value = MessageFlow.getMessageData(Url(Data.selectedAccount.tenantUrl), Data.selectedAccount.tokens.accessToken, messageLink)

            if (message.value.hasAttachments) {
                attachments.value = MessageFlow.getAttachmentList(Url(Data.selectedAccount.tenantUrl), Data.selectedAccount.tokens.accessToken, message.value.links.attachments!!.href)

                for (attachment in attachments.value) {
                    attachmentDownloadProgress.value = attachmentDownloadProgress.value + Pair(attachment.id, 0f)
                }
            }
        }
    }

    override fun downloadAttachment(attachment: Attachment) {
        scope.launch {
            val response = requestGET(
                URLBuilder(Data.selectedAccount.tenantUrl).appendEncodedPathSegments(attachment.links.downloadLink.href).build(),
                accessToken = Data.selectedAccount.tokens.accessToken,
                onDownload = { bytesSentTotal, contentLength ->
                    attachmentDownloadProgress.value = attachmentDownloadProgress.value + Pair(attachment.id, bytesSentTotal.toFloat() / contentLength.toFloat())
                }
            ).readBytes()

            writeFile(attachment.id.toString(), attachment.name, response)
            openFileFromCache(attachment.id.toString(), attachment.name)

            attachmentDownloadProgress.value = attachmentDownloadProgress.value.toMutableMap().also {
                it.remove(attachment.id)
            }
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