package nl.tiebe.otarium.logic.home.children.messages

import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import nl.tiebe.otarium.logic.data.wrapper.messages.MessageFolder
import nl.tiebe.otarium.logic.data.wrapper.messages.MessageItem
import nl.tiebe.otarium.logic.home.HomeComponent
import nl.tiebe.otarium.logic.home.children.messages.children.message.children.ReceiverInfoComponent

/**
 * Interface for the implementation of the backend for the messages UI.
 *
 * @param MessageItem The type of the message.
 * @param MessageFolder The type of the folder.
 */
interface MessagesComponent: HomeComponent.MenuItemComponent {
    /** The stack navigation */
    val navigation: StackNavigation<Config>

    /**
     * Get the available folders.
     *
     * @return A list of the available folders.
     */
    suspend fun getFolders(): List<MessageFolder>

    /**
     * Navigate to a submenu.
     *
     * @param child The config for that submenu child.
     */
    fun navigate(child: Config) {
        navigation.push(child)
    }

    /**
     * Navigate to the given folder.
     *
     * @param folder The folder to navigate to.
     */
    fun navigateToFolder(folder: MessageFolder) = navigate(Config.Folder(folder))

    /**
     * Navigate to the given message.
     *
     * @param message The message to navigate to.
     */
    fun navigateToMessage(message: MessageItem) {
        navigate(Config.Message(message))
    }

    /** The folder available on the server. */
    val folders: MutableValue<List<MessageFolder>>

    /**
     * The possible menus.
     */
    sealed class Config : Parcelable {
        /**
         * The main menu, will probably stop existing in the near future, in favor of just going to the first folder.
         */
        @Deprecated("Should go to the first folder instead")
        @Parcelize
        data object Main : Config()

        /**
         * Shows the folder contents, with all the messages.
         *
         * @param folderId The id of the folder to show.
         */
        @Parcelize
        data class Folder(val folder: MessageFolder) : Config()

        /**
         * Shows the message.
         *
         * @param message The link to the message.
         */
        @Parcelize
        data class Message(val message: MessageItem) : Config()

        /**
         * Shows information about the receiver of a message.
         *
         * @param message The link to the message.
         * @param receiverType The type of the receiver. (Main, CC, BCC)
         */
        @Parcelize
        data class ReceiverInfo(val message: MessageItem, val receiverType: ReceiverInfoComponent.ReceiverType) : Config()
    }
}