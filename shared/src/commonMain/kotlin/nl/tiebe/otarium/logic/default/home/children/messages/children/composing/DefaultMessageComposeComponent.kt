package nl.tiebe.otarium.logic.default.home.children.messages.children.composing

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.mohamedrejeb.richeditor.model.RichTextState
import dev.tiebe.magisterapi.api.account.ProfileInfoFlow
import dev.tiebe.magisterapi.api.messages.MessageFlow
import dev.tiebe.magisterapi.response.profileinfo.Contact
import io.ktor.http.*
import kotlinx.coroutines.launch
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.logic.default.componentCoroutineScope
import nl.tiebe.otarium.logic.default.home.children.messages.DefaultMessagesComponent
import nl.tiebe.otarium.logic.root.home.children.messages.children.composing.MessageComposeComponent
import nl.tiebe.otarium.ui.utils.ContactChip
import nl.tiebe.otarium.ui.utils.chips.ChipTextFieldState

class DefaultMessageComposeComponent(
    componentContext: ComponentContext,
    override val parentComponent: DefaultMessagesComponent,
    prefilledSubject: String? = null,
    prefilledBody: String? = null,
    prefilledTo: List<Int> = emptyList(),
    prefilledCc: List<Int> = emptyList(),
    prefilledBcc: List<Int> = emptyList(),
): MessageComposeComponent, ComponentContext by componentContext {
    val scope = componentCoroutineScope()
    override val contactList: MutableValue<List<Contact>> = MutableValue(listOf())

    override val toList = ChipTextFieldState<ContactChip>()
    override val ccList = ChipTextFieldState<ContactChip>()
    override val bccList = ChipTextFieldState<ContactChip>()
    override val subject: MutableValue<String> = MutableValue(prefilledSubject ?: "")
    override val body = RichTextState().apply {
        if (prefilledBody != null) {
            setHtml(prefilledBody)
        }
    }

    override suspend fun send() {
        MessageFlow.sendMessage(
            Url(Data.selectedAccount.tenantUrl),
            Data.selectedAccount.tokens.accessToken,
            subject.value,
            body.toHtml(),
            toList.chips.map { it.contact.id },
            ccList.chips.map { it.contact.id },
            bccList.chips.map { it.contact.id }
        )
    }

    init {
        scope.launch {
            contactList.value = ProfileInfoFlow.getContacts(Data.selectedAccount.tenantUrl, Data.selectedAccount.tokens.accessToken)
            prefilledTo.mapNotNull { id -> contactList.value.find { it.id == id } }.forEach {
                toList.addChip(ContactChip(it))
            }
            prefilledCc.mapNotNull { id -> contactList.value.find { it.id == id } }.forEach {
                ccList.addChip(ContactChip(it))
            }
            prefilledBcc.mapNotNull { id -> contactList.value.find { it.id == id } }.forEach {
                bccList.addChip(ContactChip(it))
            }
        }
    }
}