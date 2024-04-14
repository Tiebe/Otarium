package nl.tiebe.otarium.logic.root.home.children.messages.children.composing

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.response.profileinfo.Contact
import nl.tiebe.otarium.logic.root.home.children.messages.MessagesComponent
import nl.tiebe.otarium.ui.utils.ContactChip
import nl.tiebe.otarium.ui.utils.chips.ChipTextFieldState

interface MessageComposeComponent {
    val parentComponent: MessagesComponent

    val contactList: Value<List<Contact>>

    val toList: ChipTextFieldState<ContactChip>
    val ccList: ChipTextFieldState<ContactChip>
    val bccList: ChipTextFieldState<ContactChip>

    val subject: MutableValue<String>
    val body: MutableValue<String>

    fun back() = parentComponent::back

    suspend fun send()

}