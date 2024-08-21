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
import nl.tiebe.otarium.logic.root.home.children.messages.children.folder.FolderSearchComponent

class DefaultFolderSearchComponent(
    componentContext: ComponentContext, override val folder: MessageFolder, val allFolders: List<MessageFolder>,
    override val parentComponent: MessagesComponent
): FolderSearchComponent, ComponentContext by componentContext {
    private suspend fun getMessages(amount: Int = 100, skip: Int = 0, search: String = ""): List<Message> {
        return MessageFlow.getMessages(Url(Data.selectedAccount.tenantUrl), Data.selectedAccount.tokens.accessToken, folder.links.messagesLink, amount, skip, search)
    }

    override val searchQuery: MutableValue<String> = MutableValue("")
    override val searchActive: MutableValue<Boolean> = MutableValue(true)
    override val searchedItems: MutableValue<List<Message>> = MutableValue(listOf())

    override fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    override suspend fun search(query: String) {
        if (query.length >= 2) searchedItems.value = getMessages(500, search = query)
        else searchedItems.value = listOf()
    }


    override fun setSearchActive(active: Boolean) {
        searchActive.value = active
    }

}