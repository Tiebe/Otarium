package nl.tiebe.otarium.ui.home.elo.children.studyguides

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
import dev.tiebe.magisterapi.response.messages.MessageFolder
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.ui.home.elo.ELOChildComponent
import nl.tiebe.otarium.ui.home.elo.children.studyguides.folder.DefaultStudyGuideFolderComponent
import nl.tiebe.otarium.ui.home.elo.children.studyguides.folder.StudyGuideFolderComponent
import nl.tiebe.otarium.ui.home.elo.children.studyguides.listscreen.DefaultStudyGuideListComponent
import nl.tiebe.otarium.ui.home.elo.children.studyguides.listscreen.StudyGuideListComponent
import nl.tiebe.otarium.ui.root.componentCoroutineScope

interface StudyGuidesChildComponent : ELOChildComponent {
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

    val folders: Value<List<MessageFolder>>

    sealed class Child {
        class StudyGuideListChild(val component: StudyGuideListComponent) : Child()
        class FolderChild(val component: StudyGuideFolderComponent) : Child()
    }

    sealed class Config : Parcelable {
        @Parcelize
        object StudyGuideList : Config()

        @Parcelize
        data class Folder(val folderId: Int) : Config()
    }

}

class DefaultStudyGuidesChildComponent(componentContext: ComponentContext) : StudyGuidesChildComponent, ComponentContext by componentContext {
    override val refreshState: MutableValue<Boolean> = MutableValue(false)

    override val scope: CoroutineScope = componentCoroutineScope()
    override val folders: MutableValue<List<MessageFolder>> = MutableValue(listOf())

    override val navigation = StackNavigation<StudyGuidesChildComponent.Config>()

    override val childStack: Value<ChildStack<StudyGuidesChildComponent.Config, StudyGuidesChildComponent.Child>> =
        childStack(
            source = navigation,
            initialConfiguration = StudyGuidesChildComponent.Config.StudyGuideList,
            handleBackButton = true, // Pop the back stack on back button press
            childFactory = ::createChild,
        )

    private fun createChild(config: StudyGuidesChildComponent.Config, componentContext: ComponentContext): StudyGuidesChildComponent.Child =
        when (config) {
            is StudyGuidesChildComponent.Config.StudyGuideList -> StudyGuidesChildComponent.Child.StudyGuideListChild(createStudyGuideListComponent(this))
            is StudyGuidesChildComponent.Config.Folder -> StudyGuidesChildComponent.Child.FolderChild(createFolderComponent(componentContext, let { if (folders.value.isEmpty()) runBlocking { getFoldersAsync() }; folders.value.first { it.id == config.folderId } }))
        }

    private fun createStudyGuideListComponent(componentContext: ComponentContext) =
        DefaultStudyGuideListComponent(
            componentContext = componentContext,
            parentComponent = this
        )


    private fun createFolderComponent(componentContext: ComponentContext, folder: MessageFolder) =
        DefaultStudyGuideFolderComponent(
            componentContext = componentContext,
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

interface StudyGuideChildScreen