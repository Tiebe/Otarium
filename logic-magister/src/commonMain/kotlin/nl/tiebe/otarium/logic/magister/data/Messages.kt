package nl.tiebe.otarium.logic.magister.data

import dev.tiebe.magisterapi.api.messages.MessageFlow
import dev.tiebe.magisterapi.response.messages.Message
import io.ktor.http.*
import nl.tiebe.otarium.utils.sendNotification

suspend fun MagisterAccount.refreshMessages(notification: (String, String) -> Unit = { title, message -> sendNotification(title, message) }): List<Message> {
    val newMessages = MessageFlow.getMessages(Url(tenantUrl), tokens.accessToken, messageFolders.first { it.id == 1 }.links.messagesLink, 50, 0)

    val updated: MutableList<Message> = messages.toMutableList()

    newMessages@ for (message in newMessages) {
        for (oldMessage in updated) {
            if (message.id == oldMessage.id) {
                continue@newMessages
            }
        }

        updated.add(message)

        if (message.hasBeenRead) continue@newMessages
        notification(
            "Nieuw bericht van ${message.sender?.name?.trim()}: ${message.subject.trim()}", ""
        )
    }

    messages = updated

    return updated
}