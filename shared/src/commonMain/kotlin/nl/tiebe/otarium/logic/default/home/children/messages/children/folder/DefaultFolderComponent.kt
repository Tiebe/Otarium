package nl.tiebe.otarium.logic.default.home.children.messages.children.folder

import androidx.compose.foundation.ScrollState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.doOnResume
import dev.tiebe.magisterapi.api.messages.MessageFlow
import dev.tiebe.magisterapi.response.messages.Message
import dev.tiebe.magisterapi.response.messages.MessageFolder
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.logic.default.componentCoroutineScope
import nl.tiebe.otarium.logic.root.home.children.messages.MessagesComponent
import nl.tiebe.otarium.logic.root.home.children.messages.children.folder.FolderComponent

class DefaultFolderComponent(
    componentContext: ComponentContext, override val folder: MessageFolder, val allFolders: List<MessageFolder>,
    override val parentComponent: MessagesComponent
): FolderComponent, ComponentContext by componentContext {
    override val refreshState: MutableValue<Boolean> = MutableValue(false)
    override val scrollState: ScrollState = ScrollState(0)

    override val scope: CoroutineScope = componentCoroutineScope()
    override val subFolders: MutableValue<List<MessageFolder>> = MutableValue(allFolders.filter { it.parentId == folder.id })
    override val messages: MutableValue<List<Message>> = MutableValue(listOf())

    private suspend fun getMessages(amount: Int = 100, skip: Int = 0): List<Message> {
        return MessageFlow.getMessages(Url(Data.selectedAccount.tenantUrl), Data.selectedAccount.tokens.accessToken, folder.links.messagesLink, amount, skip)
    }

    override fun refresh() {
        if (folder.id == -1) return

        scope.launch {
            refreshState.value = true
            parentComponent.getFoldersAsync()

            messages.value = getMessages()
            refreshState.value = false
        }
    }

    override fun loadNewMessages() {
        scope.launch {
            refreshState.value = true
            messages.value += getMessages(skip = messages.value.size)
            refreshState.value = false
        }
    }

    override val searchQuery: MutableValue<String> = MutableValue("")
    override val searchActive: MutableValue<Boolean> = MutableValue(false)
    override val searchedItems: MutableValue<List<Message>> = MutableValue(listOf())

    override fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    override fun search(query: String) {
        // TODO: search messages
    }


    override fun setSearchActive(active: Boolean) {
        searchActive.value = active
    }

    init {
        lifecycle.doOnResume {
            refresh()
        }
    }

}