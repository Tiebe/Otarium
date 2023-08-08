package nl.tiebe.otarium.logic.home.children.elo.children.studyguides

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import dev.tiebe.magisterapi.response.studyguide.StudyGuide
import nl.tiebe.otarium.logic.root.home.children.elo.ELOComponent
import nl.tiebe.otarium.logic.root.home.children.elo.children.studyguides.children.folder.StudyGuideFolderComponent
import nl.tiebe.otarium.logic.root.home.children.elo.children.studyguides.children.list.StudyGuideListComponent
import kotlinx.coroutines.CoroutineScope

interface StudyGuidesChildComponent : ELOComponent.ELOChildComponent {
    val navigation: StackNavigation<Config>
    val childStack: Value<ChildStack<Config, Child>>

    val refreshState: Value<Boolean>
    val scope: CoroutineScope

    fun navigate(child: Config) {
        navigation.push(child)
    }

    val studyGuides: Value<List<StudyGuide>>

    sealed class Child {
        class StudyGuideListChild(val component: StudyGuideListComponent) : Child()
        class FolderChild(val component: StudyGuideFolderComponent) : Child()
    }

    sealed class Config : Parcelable {
        @Parcelize
        object StudyGuideList : Config()

        @Parcelize
        data class StudyGuide(val studyGuideLink: String) : Config()
    }


    val onBack: MutableValue<() -> Unit>

    fun registerBackHandler()
    fun unregisterBackHandler()


    interface StudyGuideChildScreen
}
