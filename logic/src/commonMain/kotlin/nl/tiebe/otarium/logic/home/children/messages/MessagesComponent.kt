package nl.tiebe.otarium.logic.home.children.messages

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import dev.tiebe.magisterapi.response.messages.Message
import dev.tiebe.magisterapi.response.messages.MessageFolder
import nl.tiebe.otarium.logic.root.home.HomeComponent
import nl.tiebe.otarium.logic.root.home.children.messages.children.folder.FolderComponent
import nl.tiebe.otarium.logic.root.home.children.messages.children.message.MessageComponent
import nl.tiebe.otarium.logic.root.home.children.messages.children.message.children.ReceiverInfoComponent
import kotlinx.coroutines.CoroutineScope

interface MessagesComponent: HomeComponent.MenuItemComponent {
    val navigation: StackNavigation<Config>
    val childStack: Value<ChildStack<Config, Child>>

    val refreshState: Value<Boolean>
    val scope: CoroutineScope

    val onBack: MutableValue<() -> Unit>


    suspend fun getFoldersAsync()
    fun getFolders()

    fun navigate(child: Config) {
        navigation.push(child)
    }

    fun navigateToFolder(folder: MessageFolder) {
        navigate(Config.Folder(folder.id))
    }

    fun navigateToMessage(message: Message) {
        navigate(Config.Message(message.links.self?.href ?: return))
    }

    val folders: Value<List<MessageFolder>>

    sealed class Child {
        class MainChild(val component: MessagesComponent) : Child()
        class FolderChild(val component: FolderComponent) : Child()
        class MessageChild(val component: MessageComponent) : Child()
        class ReceiverInfoChild(val component: ReceiverInfoComponent) : Child()
    }

    sealed class Config : Parcelable {
        @Parcelize
        object Main : Config()

        @Parcelize
        data class Folder(val folderId: Int) : Config()

        @Parcelize
        data class Message(val messageLink: String) : Config()

        @Parcelize
        data class ReceiverInfo(val messageLink: String, val receiverType: ReceiverInfoComponent.ReceiverType) : Config()
    }

    fun registerBackHandler()
    fun unregisterBackHandler()
}