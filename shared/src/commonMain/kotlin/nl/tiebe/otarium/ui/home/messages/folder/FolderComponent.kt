package nl.tiebe.otarium.ui.home.messages.folder

import androidx.compose.foundation.ScrollState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.google.accompanist.swiperefresh.SwipeRefreshState
import dev.tiebe.magisterapi.api.messages.MessageFlow
import dev.tiebe.magisterapi.response.messages.Message
import dev.tiebe.magisterapi.response.messages.MessageFolder
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.ui.home.messages.MessagesComponent
import nl.tiebe.otarium.ui.root.componentCoroutineScope

interface FolderComponent {
    val refreshState: SwipeRefreshState
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
    override val refreshState: SwipeRefreshState = SwipeRefreshState(isRefreshing = false)
    override val scrollState: ScrollState = ScrollState(0)

    override val scope: CoroutineScope = componentCoroutineScope()
    override val subFolders: MutableValue<List<MessageFolder>> = MutableValue(allFolders.filter { it.parentId == folder.id })
    override val messages: MutableValue<List<Message>> = MutableValue(listOf())

    suspend fun getMessages(amount: Int = 20, skip: Int = 0): List<Message> {
        return MessageFlow.getMessages(Url(Data.selectedAccount.tenantUrl), Data.selectedAccount.tokens.accessToken, folder.links.messagesLink, amount, skip)
    }

    override fun refresh() {
        scope.launch {
            refreshState.isRefreshing = true
            messages.value = getMessages()
            refreshState.isRefreshing = false
        }
    }

    override fun loadNewMessages() {
        scope.launch {
            refreshState.isRefreshing = true
            messages.value = messages.value + getMessages(skip = messages.value.size)
            refreshState.isRefreshing = false
        }
    }

    init {
        refresh()
    }

}