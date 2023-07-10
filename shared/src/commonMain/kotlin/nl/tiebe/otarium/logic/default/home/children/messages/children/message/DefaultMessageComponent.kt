package nl.tiebe.otarium.logic.default.home.children.messages.children.message

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import dev.tiebe.magisterapi.api.messages.MessageFlow
import dev.tiebe.magisterapi.response.messages.Attachment
import dev.tiebe.magisterapi.response.messages.MessageData
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.logic.default.componentCoroutineScope
import nl.tiebe.otarium.logic.root.home.children.messages.MessagesComponent
import nl.tiebe.otarium.logic.root.home.children.messages.children.message.MessageComponent
import nl.tiebe.otarium.logic.root.home.unreadMessages
import nl.tiebe.otarium.utils.openFileFromCache
import nl.tiebe.otarium.utils.requestGET
import nl.tiebe.otarium.utils.writeFile

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
                    attachmentDownloadProgress.value += Pair(attachment.id, 0f)
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
                    attachmentDownloadProgress.value += Pair(
                        attachment.id,
                        bytesSentTotal.toFloat() / contentLength.toFloat()
                    )
                }
            ).readBytes()

            writeFile(attachment.id.toString(), attachment.name, response)
            openFileFromCache(attachment.id.toString(), attachment.name)

            attachmentDownloadProgress.value = attachmentDownloadProgress.value.toMutableMap().also {
                it.remove(attachment.id)
            }
        }
    }

    override fun deleteMessage() {
        scope.launch {
            if (message.value.folderId == 3) {
                MessageFlow.permanentlyDeleteMessage(
                    Url(Data.selectedAccount.tenantUrl),
                    Data.selectedAccount.tokens.accessToken,
                    message.value.id
                )
            } else {
                MessageFlow.deleteMessage(
                    Url(Data.selectedAccount.tenantUrl),
                    Data.selectedAccount.tokens.accessToken,
                    message.value.id
                )
            }
            parentComponent.back()
        }
    }

    override fun restoreMessage() {
        scope.launch {
            MessageFlow.moveMessage(
                Url(Data.selectedAccount.tenantUrl),
                Data.selectedAccount.tokens.accessToken,
                message.value.id,
                1
            )
        }
    }

    private fun markAsRead(it: MessageData) {
        if (it.id != 0) {
            scope.launch {
                if (!it.hasBeenRead) unreadMessages.value -= 1

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
        message.unsubscribe(::markAsRead)
    }

    init {
        message.subscribe(::markAsRead)

        getMessage()
    }

}