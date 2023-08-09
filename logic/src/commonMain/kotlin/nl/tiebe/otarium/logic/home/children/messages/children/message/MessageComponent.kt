package nl.tiebe.otarium.logic.home.children.messages.children.message

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.response.messages.Attachment
import dev.tiebe.magisterapi.response.messages.MessageData
import nl.tiebe.otarium.logic.home.children.messages.MessagesComponent

/**
 * Interface for the backend of the message screen.
 */
interface MessageComponent {
    /** The parent component. */
    val parentComponent: MessagesComponent

    /** The message link. */
    val messageLink: String

    /** The message data. */
    val message: MutableValue<MessageData>

    /** The message attachments. If there are no attachments in this list, the UI item should be hidden. */
    val attachments: Value<List<Attachment>>

    /** The message attachment download progress. A map from the attachment id to the progress float (0.0-1.0) */
    val attachmentDownloadProgress: Value<Map<Int, Float>>

    /**
     * Download an attachment. The progress can be tracked using [attachmentDownloadProgress].
     *
     * @param attachment The attachment to download.
     */
    fun downloadAttachment(attachment: Attachment)
}