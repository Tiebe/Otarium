package nl.tiebe.otarium.logic.magister.data.wrapper.messages

import nl.tiebe.otarium.logic.magister.data.wrapper.MagisterMessage
import nl.tiebe.otarium.logic.magister.data.wrapper.MessageItemWrapper

class MessageItem(val message: MagisterMessage) : MessageItemWrapper {
    override val id: Int = message.id

}