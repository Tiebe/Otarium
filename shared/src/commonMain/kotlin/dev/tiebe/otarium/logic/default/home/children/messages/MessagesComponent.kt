package dev.tiebe.otarium.logic.default.home.children.messages

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import dev.tiebe.magisterapi.api.messages.MessageFlow
import dev.tiebe.magisterapi.response.messages.MessageFolder
import dev.tiebe.otarium.Data
import dev.tiebe.otarium.logic.default.componentCoroutineScope
import dev.tiebe.otarium.logic.default.home.children.messages.children.folder.DefaultFolderComponent
import dev.tiebe.otarium.logic.default.home.children.messages.children.message.DefaultMessageComponent
import dev.tiebe.otarium.logic.default.home.children.messages.children.message.children.DefaultReceiverInfoComponent
import dev.tiebe.otarium.logic.root.home.children.messages.MessagesComponent
import dev.tiebe.otarium.logic.root.home.children.messages.children.message.children.ReceiverInfoComponent
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DefaultMessagesComponent(
    componentContext: ComponentContext
): MessagesComponent, ComponentContext by componentContext {
    override val refreshState: MutableValue<Boolean> = MutableValue(false)

    override val scope: CoroutineScope = componentCoroutineScope()
    override val onBack: MutableValue<() -> Unit> = MutableValue {}

    override val folders: MutableValue<List<MessageFolder>> = MutableValue(listOf())

    override val navigation = StackNavigation<MessagesComponent.Config>()

    override val childStack: Value<ChildStack<MessagesComponent.Config, MessagesComponent.Child>> =
        childStack(
            source = navigation,
            initialConfiguration = MessagesComponent.Config.Main,
            handleBackButton = false, // Pop the back stack on back button press
            childFactory = ::createChild,
        )

    private fun createChild(config: MessagesComponent.Config, componentContext: ComponentContext): MessagesComponent.Child =
        when (config) {
            is MessagesComponent.Config.Main -> MessagesComponent.Child.MainChild(this)
            is MessagesComponent.Config.Folder -> MessagesComponent.Child.FolderChild(
                createFolderComponent(
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

    private val registered = MutableValue(false)
    private val backCallback = BackCallback { onBack.value() }

    override fun registerBackHandler() {
        if (registered.value) return
        backHandler.register(backCallback)
        registered.value = true
    }

    override fun unregisterBackHandler() {
        if (!registered.value) return
        backHandler.unregister(backCallback)
        registered.value = false
    }


    init {
        childStack.subscribe { childStack ->
            if (childStack.backStack.isEmpty()) {
                unregisterBackHandler()
            } else {
                registerBackHandler()
            }
        }

        scope.launch {
            getFolders()
        }
    }

}