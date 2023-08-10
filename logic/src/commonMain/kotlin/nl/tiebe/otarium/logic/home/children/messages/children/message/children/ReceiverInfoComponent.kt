package nl.tiebe.otarium.logic.home.children.messages.children.message.children

import com.arkivanov.decompose.value.MutableValue
import nl.tiebe.otarium.logic.data.wrapper.messages.MessageData
import nl.tiebe.otarium.logic.home.children.messages.MessagesComponent

/**
 * Interface for the implementation of the backend for the receiver info UI.
 */
interface ReceiverInfoComponent {
    /** The parent component */
    val parentComponent: MessagesComponent

    /** The message */
    val message: MutableValue<MessageData>

    /** The receiver type */
    val receiverType: ReceiverType

    /**
     * The possible receiver types.
     */
    enum class ReceiverType {
        NORMAL, CC, BCC
    }
}
