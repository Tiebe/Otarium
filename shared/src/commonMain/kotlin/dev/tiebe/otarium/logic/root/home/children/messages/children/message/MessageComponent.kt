package dev.tiebe.otarium.logic.root.home.children.messages.children.message

import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.response.messages.Attachment
import dev.tiebe.magisterapi.response.messages.MessageData
import dev.tiebe.otarium.logic.root.home.children.messages.MessagesComponent

interface MessageComponent {
    val parentComponent: MessagesComponent
    val messageLink: String

    val message: Value<MessageData>
    val attachments: Value<List<Attachment>>
    val attachmentDownloadProgress: Value<Map<Int, Float>>

    fun downloadAttachment(attachment: Attachment)
}