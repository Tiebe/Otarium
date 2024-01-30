package nl.tiebe.otarium.logic.root.home.children.messages.children.message.children

import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.response.messages.MessageData
import nl.tiebe.otarium.logic.root.home.children.messages.MessagesComponent

interface ReceiverInfoComponent {
    val parentComponent: MessagesComponent
    val messageLink: String

    val message: Value<MessageData>

    val receiverType: ReceiverType

    enum class ReceiverType {
        NORMAL, CC, BCC
    }
}
