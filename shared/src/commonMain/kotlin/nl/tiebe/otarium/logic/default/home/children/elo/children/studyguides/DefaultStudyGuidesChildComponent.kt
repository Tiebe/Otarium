package nl.tiebe.otarium.logic.default.home.children.elo.children.studyguides

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import dev.tiebe.magisterapi.response.studyguide.StudyGuide
import kotlinx.coroutines.CoroutineScope
import nl.tiebe.otarium.logic.default.componentCoroutineScope
import nl.tiebe.otarium.logic.default.home.children.elo.children.studyguides.children.folder.DefaultStudyGuideFolderComponent
import nl.tiebe.otarium.logic.default.home.children.elo.children.studyguides.children.list.DefaultStudyGuideListComponent
import nl.tiebe.otarium.logic.root.home.children.elo.children.studyguides.StudyGuidesChildComponent

class DefaultStudyGuidesChildComponent(componentContext: ComponentContext) : StudyGuidesChildComponent, ComponentContext by componentContext, BackHandlerOwner {
    override val refreshState: MutableValue<Boolean> = MutableValue(false)

    override val scope: CoroutineScope = componentCoroutineScope()
    override val studyGuides: MutableValue<List<StudyGuide>> = MutableValue(listOf())

    override val navigation = StackNavigation<StudyGuidesChildComponent.Config>()

    override val childStack: Value<ChildStack<StudyGuidesChildComponent.Config, StudyGuidesChildComponent.Child>> =
        childStack(
            source = navigation,
            serializer = StudyGuidesChildComponent.Config.serializer(),
            initialConfiguration = StudyGuidesChildComponent.Config.StudyGuideList,
            key = "StudyGuideChildStack",
            handleBackButton = true, // Pop the back stack on back button press
            childFactory = ::createChild,
        )

    private fun createChild(config: StudyGuidesChildComponent.Config, componentContext: ComponentContext): StudyGuidesChildComponent.Child =
        when (config) {
            is StudyGuidesChildComponent.Config.StudyGuideList -> StudyGuidesChildComponent.Child.StudyGuideListChild(
                createStudyGuideListComponent(this)
            )
            is StudyGuidesChildComponent.Config.StudyGuide -> StudyGuidesChildComponent.Child.FolderChild(
                createStudyGuideComponent(componentContext, config.studyGuideLink)
            )
        }

    private fun createStudyGuideListComponent(componentContext: ComponentContext) =
        DefaultStudyGuideListComponent(
            componentContext = componentContext,
            parentComponent = this
        )


    private fun createStudyGuideComponent(componentContext: ComponentContext, studyGuideLink: String) =
        DefaultStudyGuideFolderComponent(
            componentContext = componentContext,
            studyGuideLink = studyGuideLink,
        )

    override fun back() {
        navigation.pop()
    }
}

interface StudyGuideChildScreen