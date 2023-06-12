package dev.tiebe.otarium.logic.root.home.children.messages.children.folder

import androidx.compose.foundation.ScrollState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnResume
import dev.tiebe.magisterapi.api.messages.MessageFlow
import dev.tiebe.magisterapi.response.messages.Message
import dev.tiebe.magisterapi.response.messages.MessageFolder
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import dev.tiebe.otarium.Data
import dev.tiebe.otarium.logic.home.children.messages.MessagesComponent
import dev.tiebe.otarium.logic.default.componentCoroutineScope

interface FolderComponent {
    val refreshState: Value<Boolean>
    val scrollState: ScrollState
    val scope: CoroutineScope

    val parentComponent: MessagesComponent

    val folder: MessageFolder

    val subFolders: Value<List<MessageFolder>>
    val messages: Value<List<Message>>
    fun refresh()
    fun loadNewMessages()
}

class DefaultFolderComponent(
    componentContext: ComponentContext, override val folder: MessageFolder, val allFolders: List<MessageFolder>,
    override val parentComponent: MessagesComponent
): FolderComponent, ComponentContext by componentContext {
    override val refreshState: MutableValue<Boolean> = MutableValue(false)
    override val scrollState: ScrollState = ScrollState(0)

    override val scope: CoroutineScope = componentCoroutineScope()
    override val subFolders: MutableValue<List<MessageFolder>> = MutableValue(allFolders.filter { it.parentId == folder.id })
    override val messages: MutableValue<List<Message>> = MutableValue(listOf())

    private suspend fun getMessages(amount: Int = 20, skip: Int = 0): List<Message> {
        return MessageFlow.getMessages(Url(Data.selectedAccount.tenantUrl), Data.selectedAccount.tokens.accessToken, folder.links.messagesLink, amount, skip)
    }

    override fun refresh() {
        scope.launch {
            refreshState.value = true
            messages.value = getMessages()
            refreshState.value = false
        }
    }

    override fun loadNewMessages() {
        scope.launch {
            refreshState.value = true
            messages.value = messages.value + getMessages(skip = messages.value.size)
            refreshState.value = false
        }
    }

    init {
        lifecycle.doOnResume {
            refresh()
        }
    }

}