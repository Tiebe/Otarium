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
import com.google.accompanist.swiperefresh.SwipeRefreshState
import dev.tiebe.magisterapi.api.messages.MessageFlow
import dev.tiebe.magisterapi.response.messages.MessageFolder
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.ui.home.MenuItemComponent
import nl.tiebe.otarium.ui.root.componentCoroutineScope

interface MessagesComponent: MenuItemComponent {
    val navigation: StackNavigation<Config>
    val childStack: Value<ChildStack<Config, Child>>

    val refreshState: SwipeRefreshState
    val scope: CoroutineScope

    var selectedFolder: MessageFolder?

    fun getFolders()

    fun navigate(child: Config) {
        navigation.push(child)
    }

    fun navigateToFolder(folder: MessageFolder) {
        selectedFolder = folder
        navigate(Config.Folder)
    }

    sealed class FoldersState {
        object Loading: FoldersState()
        data class Data(val data: List<MessageFolder>): FoldersState()
        object Failed: FoldersState()
    }

    val foldersState: Value<FoldersState>

    sealed class Child {
        class MainChild(val component: MessagesComponent) : Child()
        class FolderChild(val component: FolderComponent) : Child()
    }

    sealed class Config : Parcelable {
        @Parcelize
        object Main : Config()

        @Parcelize
        object Folder : Config()
    }
}

class DefaultMessagesComponent(
    componentContext: ComponentContext
): MessagesComponent, ComponentContext by componentContext {
    override val refreshState: SwipeRefreshState = SwipeRefreshState(isRefreshing = false)

    override val scope: CoroutineScope = componentCoroutineScope()
    override var selectedFolder: MessageFolder? = null
    override val foldersState: MutableValue<MessagesComponent.FoldersState> = MutableValue(MessagesComponent.FoldersState.Loading)

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
            is MessagesComponent.Config.Folder -> MessagesComponent.Child.FolderChild(createFolderComponent(componentContext, selectedFolder!!))
        }

    private fun createFolderComponent(componentContext: ComponentContext, folder: MessageFolder): FolderComponent {
        return DefaultFolderComponent(
            componentContext = componentContext,
            folder = folder
        )
    }

    override fun getFolders() {
        scope.launch {
            refreshState.isRefreshing = true

            try {
                foldersState.value =
                    MessagesComponent.FoldersState.Data(
                        MessageFlow.getAllFolders(
                            Url(Data.selectedAccount.tenantUrl),
                            Data.selectedAccount.tokens.accessToken
                        )
                    )
            } catch (e: Exception) {
                foldersState.value = MessagesComponent.FoldersState.Failed
                e.printStackTrace()
            }

            refreshState.isRefreshing = false
        }
    }

    init {
        getFolders()
    }

}