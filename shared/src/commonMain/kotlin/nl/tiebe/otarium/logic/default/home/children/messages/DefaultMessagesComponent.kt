package nl.tiebe.otarium.logic.default.home.children.messages

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import dev.tiebe.magisterapi.response.messages.MessageFolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.logic.default.componentCoroutineScope
import nl.tiebe.otarium.logic.default.home.children.messages.children.composing.DefaultMessageComposeComponent
import nl.tiebe.otarium.logic.default.home.children.messages.children.folder.DefaultFolderComponent
import nl.tiebe.otarium.logic.default.home.children.messages.children.folder.DefaultFolderSearchComponent
import nl.tiebe.otarium.logic.default.home.children.messages.children.message.DefaultMessageComponent
import nl.tiebe.otarium.logic.default.home.children.messages.children.message.children.DefaultReceiverInfoComponent
import nl.tiebe.otarium.logic.root.home.children.messages.MessagesComponent
import nl.tiebe.otarium.logic.root.home.children.messages.children.message.children.ReceiverInfoComponent

class DefaultMessagesComponent(
    componentContext: ComponentContext
): MessagesComponent, ComponentContext by componentContext, BackHandlerOwner {
    override val refreshState: MutableValue<Boolean> = MutableValue(false)

    override val scope: CoroutineScope = componentCoroutineScope()

    override val folders: MutableValue<List<MessageFolder>> = MutableValue(Data.selectedAccount.messageFolders)

    override val navigation = StackNavigation<MessagesComponent.Config>()

    override val childStack: Value<ChildStack<MessagesComponent.Config, MessagesComponent.Child>> =
        childStack(
            source = navigation,
            serializer = MessagesComponent.Config.serializer(),
            initialConfiguration = MessagesComponent.Config.Folder(let { if (folders.value.isEmpty()) runBlocking { getFoldersAsync() }; folders.value.first().id }),
            handleBackButton = false, // Pop the back stack on back button press
            childFactory = ::createChild,
        )

    private fun createChild(config: MessagesComponent.Config, componentContext: ComponentContext): MessagesComponent.Child =
        when (config) {
            is MessagesComponent.Config.Folder -> MessagesComponent.Child.FolderChild(
                createFolderComponent(
                    componentContext,
                    let { if (folders.value.isEmpty()) runBlocking { getFoldersAsync() }; folders.value.first { it.id == config.folderId } })
            )
            is MessagesComponent.Config.FolderSearch -> MessagesComponent.Child.FolderSearchChild(
                createFolderSearchComponent(
                    componentContext,
                    let { if (folders.value.isEmpty()) runBlocking { getFoldersAsync() }; folders.value.first { it.id == config.folderId } })
            )
            is MessagesComponent.Config.Message -> MessagesComponent.Child.MessageChild(
                createMessageComponent(
                    componentContext,
                    config.messageLink
                )
            )
            is MessagesComponent.Config.ReceiverInfo -> MessagesComponent.Child.ReceiverInfoChild(
                createReceiverInfoComponent(componentContext, config.messageLink, config.receiverType)
            )

            is MessagesComponent.Config.Compose -> MessagesComponent.Child.ComposeChild(
                createComposeComponent(componentContext, config.subject, config.body, config.receivers)
            )
        }

    private fun createFolderComponent(componentContext: ComponentContext, folder: MessageFolder) =
        DefaultFolderComponent(
            componentContext = componentContext,
            folder = folder,
            allFolders = folders.value,
            parentComponent = this
        )

    private fun createFolderSearchComponent(componentContext: ComponentContext, folder: MessageFolder) =
        DefaultFolderSearchComponent(
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

    private fun createComposeComponent(
        componentContext: ComponentContext,
        subject: String,
        body: String,
        receivers: List<Int>
    ) =
        DefaultMessageComposeComponent(
            componentContext = componentContext,
            parentComponent = this,
            prefilledSubject = subject,
            prefilledBody = body,
            prefilledTo = receivers
        )

    override suspend fun getFoldersAsync() {
        refreshState.value = true

        try {
            folders.value = Data.selectedAccount.refreshFolders()
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

    override fun back() {
        navigation.pop()
    }


    init {
        runBlocking {
            getFoldersAsync()
        }
    }

}