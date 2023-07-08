package nl.tiebe.otarium.logic.default.home.children.messages.children.composing

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import nl.tiebe.otarium.logic.default.home.children.messages.DefaultMessagesComponent
import nl.tiebe.otarium.logic.root.home.children.messages.children.composing.MessageComposeComponent

class DefaultMessageComposeComponent(
    componentContext: ComponentContext,
    override val parentComponent: DefaultMessagesComponent,
    prefilledSubject: String? = null,
    prefilledBody: String? = null,
    prefilledTo: List<String>? = null,
    prefilledCc: List<String>? = null,
    prefilledBcc: List<String>? = null,
): MessageComposeComponent, ComponentContext by componentContext {
    override val toList: MutableValue<List<String>> = MutableValue(prefilledTo ?: listOf())
    override val ccList: MutableValue<List<String>> = MutableValue(prefilledCc ?: listOf())
    override val bccList: MutableValue<List<String>> = MutableValue(prefilledBcc ?: listOf())
    override val subject: MutableValue<String> = MutableValue(prefilledSubject ?: "")
    override val body: MutableValue<String> = MutableValue(prefilledBody ?: "")

    override fun send() {
        TODO("Not yet implemented")
    }

}