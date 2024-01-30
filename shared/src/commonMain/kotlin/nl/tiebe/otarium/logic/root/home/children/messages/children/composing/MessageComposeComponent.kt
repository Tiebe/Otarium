package nl.tiebe.otarium.logic.root.home.children.messages.children.composing

import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.response.profileinfo.Contact
import nl.tiebe.otarium.logic.root.home.children.messages.MessagesComponent

interface MessageComposeComponent {
    val parentComponent: MessagesComponent

    val contactList: Value<List<Contact>>

    val toList: Value<List<String>>
    val ccList: Value<List<String>>
    val bccList: Value<List<String>>

    val subject: Value<String>
    val body: Value<String>

    fun back() = parentComponent::back

    fun send()

}