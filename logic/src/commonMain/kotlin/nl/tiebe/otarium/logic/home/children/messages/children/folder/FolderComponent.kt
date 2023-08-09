package nl.tiebe.otarium.logic.home.children.messages.children.folder

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.parcelable.Parcelable
import nl.tiebe.otarium.logic.home.children.messages.MessagesComponent

/**
 * Interface for the implementation of the backend for the folder UI.
 */
interface FolderComponent<MessageItem: Parcelable, MessageFolder: Parcelable> {
    /** The parent component */
    val parentComponent: MessagesComponent<MessageItem, MessageFolder>

    /** The folder */
    val folder: MessageFolder

    /** The subfolders of this folder. */
    val subFolders: MutableValue<List<MessageFolder>>

    /** The messages in this folder. */
    val messages: MutableValue<List<MessageItem>>

    /**
     * Refresh the messages and subfolders.
     */
    fun refresh()

    /**
     * Load the next page of messages. This should be done when the user scrolls to the bottom of the list.
     *
     * @return The new messages. These should also be saved to [messages].
     */
    fun loadNewMessages(): List<MessageItem>
}