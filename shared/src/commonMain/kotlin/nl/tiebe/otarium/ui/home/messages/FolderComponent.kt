package nl.tiebe.otarium.ui.home.messages

import com.arkivanov.decompose.ComponentContext
import dev.tiebe.magisterapi.response.messages.MessageFolder
import kotlinx.coroutines.CoroutineScope
import nl.tiebe.otarium.ui.root.componentCoroutineScope

interface FolderComponent {
    val scope: CoroutineScope

    val folder: MessageFolder
}

class DefaultFolderComponent(
    componentContext: ComponentContext, override val folder: MessageFolder
): FolderComponent, ComponentContext by componentContext {
    override val scope: CoroutineScope = componentCoroutineScope()

}