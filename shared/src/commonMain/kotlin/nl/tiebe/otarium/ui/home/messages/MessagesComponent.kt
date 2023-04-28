package nl.tiebe.otarium.ui.home.messages

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import dev.tiebe.magisterapi.api.messages.MessageFlow
import dev.tiebe.magisterapi.response.messages.Message
import dev.tiebe.magisterapi.response.messages.MessageFolder
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.ui.home.MenuItemComponent
import nl.tiebe.otarium.ui.home.messages.folder.DefaultFolderComponent
import nl.tiebe.otarium.ui.home.messages.folder.FolderComponent
import nl.tiebe.otarium.ui.home.messages.message.DefaultMessageComponent
import nl.tiebe.otarium.ui.home.messages.message.MessageComponent
import nl.tiebe.otarium.ui.home.messages.message.receiver.DefaultReceiverInfoComponent
import nl.tiebe.otarium.ui.home.messages.message.receiver.ReceiverInfoComponent
import nl.tiebe.otarium.ui.root.componentCoroutineScope

interface MessagesComponent: MenuItemComponent {
    val navigation: StackNavigation<Config>
    val childStack: Value<ChildStack<Config, Child>>

    val refreshState: Value<Boolean>
    val scope: CoroutineScope

    suspend fun getFoldersAsync()
    fun getFolders()

    fun navigate(child: Config) {
        navigation.push(child)
    }

    fun navigateToFolder(folder: MessageFolder) {
        navigate(Config.Folder(folder.id))
    }

    fun navigateToMessage(message: Message) {
        navigate(Config.Message(message.links.self.href))
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
}

class DefaultMessagesComponent(
    componentContext: ComponentContext
): MessagesComponent, ComponentContext by componentContext {
    override val refreshState: MutableValue<Boolean> = MutableValue(false)

    override val scope: CoroutineScope = componentCoroutineScope()
    override val folders: MutableValue<List<MessageFolder>> = MutableValue(listOf())

    override val navigation = StackNavigation<MessagesComponent.Config>()

    override val childStack: Value<ChildStack<MessagesComponent.Config, MessagesComponent.Child>> =
        childStack(
            source = navigation,
            initialConfiguration = MessagesComponent.Config.Main,
            handleBackButton = true, // Pop the back stack on back button press
            childFactory = ::createChild,
        )

    private fun createChild(config: MessagesComponent.Config, componentContext: ComponentContext): MessagesComponent.Child =
        when (config) {
            is MessagesComponent.Config.Main -> MessagesComponent.Child.MainChild(this)
            is MessagesComponent.Config.Folder -> MessagesComponent.Child.FolderChild(createFolderComponent(componentContext, let { if (folders.value.isEmpty()) runBlocking { getFoldersAsync() }; folders.value.first { it.id == config.folderId } }))
            is MessagesComponent.Config.Message -> MessagesComponent.Child.MessageChild(createMessageComponent(componentContext, config.messageLink))
            is MessagesComponent.Config.ReceiverInfo -> MessagesComponent.Child.ReceiverInfoChild(createReceiverInfoComponent(componentContext, config.messageLink, config.receiverType))
        }

    private fun createFolderComponent(componentContext: ComponentContext, folder: MessageFolder) =
        DefaultFolderComponent(
            componentContext = componentContext,
            folder = folder,
            allFolders = folders.value,
            parentComponent = this
        )

    private fun createMessageComponent(componentContext: ComponentContext, messageLink: String) =
        DefaultMessageComponent(
            componentContext = componentContext,
            messageLink = messageLink,
            parentComponent = this
        )

    private fun createReceiverInfoComponent(componentContext: ComponentContext, messageLink: String, receiverType: ReceiverInfoComponent.ReceiverType) =
        DefaultReceiverInfoComponent(
            componentContext = componentContext,
            messageLink = messageLink,
            parentComponent = this,
            receiverType = receiverType
        )

    override suspend fun getFoldersAsync() {
        refreshState.value = true

        try {
            folders.value =
                MessageFlow.getAllFolders(
                    Url(Data.selectedAccount.tenantUrl),
                    Data.selectedAccount.tokens.accessToken
                )
        } catch (e: Exception) {
            e.printStackTrace()
        }

        refreshState.value = false
    }

    override fun getFolders() {
        scope.launch {
            getFoldersAsync()
        }
    }

    init {
        scope.launch {
            getFolders()
        }
    }

}