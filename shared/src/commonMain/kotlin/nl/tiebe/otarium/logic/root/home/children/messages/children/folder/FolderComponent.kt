package nl.tiebe.otarium.logic.root.home.children.messages.children.folder

import androidx.compose.foundation.ScrollState
import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.response.messages.Message
import dev.tiebe.magisterapi.response.messages.MessageFolder
import kotlinx.coroutines.CoroutineScope
import nl.tiebe.otarium.logic.root.home.children.messages.MessagesComponent

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

    val searchQuery: Value<String>
    val searchActive: Value<Boolean>
    val searchedItems: Value<List<Message>>
    fun setSearchQuery(query: String)
    fun search(query: String)
    fun setSearchActive(active: Boolean)
}