package nl.tiebe.otarium.logic.home.children.messages.children.message.children

import com.arkivanov.decompose.value.MutableValue
import nl.tiebe.otarium.logic.home.children.messages.MessagesComponent

/**
 * Interface for the implementation of the backend for the receiver info UI.
 */
interface ReceiverInfoComponent<MessageItem, MessageFolder, MessageExtraData: Any> {
    /** The parent component */
    val parentComponent: MessagesComponent<MessageItem, MessageFolder>

    /** The message */
    val message: MutableValue<MessageExtraData>

    /** The receiver type */
    val receiverType: ReceiverType

    /**
     * The possible receiver types.
     */
    enum class ReceiverType {
        NORMAL, CC, BCC
    }
}
