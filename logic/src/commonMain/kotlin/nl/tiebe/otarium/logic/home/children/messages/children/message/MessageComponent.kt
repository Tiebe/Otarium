package nl.tiebe.otarium.logic.home.children.messages.children.message

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import nl.tiebe.otarium.logic.home.children.messages.MessagesComponent

/**
 * Interface for the backend of the message screen.
 *
 * @param MessageItem The type of the message item.
 * @param MessageFolder The type of the message folder.
 * @param MessageExtraData The type of extra message data.
 * @param Attachment The type of a message attachment.
 */
interface MessageComponent<MessageItem: Parcelable, MessageFolder: Parcelable, MessageExtraData: Any, Attachment: Any> {
    /** The parent component. */
    val parentComponent: MessagesComponent<MessageItem, MessageFolder>

    /** The message data. */
    val message: MutableValue<MessageExtraData>

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