package nl.tiebe.otarium.ui.home.elo.children.studyguides

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import dev.tiebe.magisterapi.response.studyguide.StudyGuide
import kotlinx.coroutines.CoroutineScope
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

}

class DefaultStudyGuidesChildComponent(componentContext: ComponentContext) : StudyGuidesChildComponent, ComponentContext by componentContext {
    override val refreshState: MutableValue<Boolean> = MutableValue(false)

    override val scope: CoroutineScope = componentCoroutineScope()
    override val studyGuides: MutableValue<List<StudyGuide>> = MutableValue(listOf())

    override val navigation = StackNavigation<StudyGuidesChildComponent.Config>()

    override val childStack: Value<ChildStack<StudyGuidesChildComponent.Config, StudyGuidesChildComponent.Child>> =
        childStack(
            source = navigation,
            initialConfiguration = StudyGuidesChildComponent.Config.StudyGuideList,
            key = "StudyGuideChildStack",
            handleBackButton = true, // Pop the back stack on back button press
            childFactory = ::createChild,
        )

    private fun createChild(config: StudyGuidesChildComponent.Config, componentContext: ComponentContext): StudyGuidesChildComponent.Child =
        when (config) {
            is StudyGuidesChildComponent.Config.StudyGuideList -> StudyGuidesChildComponent.Child.StudyGuideListChild(createStudyGuideListComponent(this))
            is StudyGuidesChildComponent.Config.StudyGuide -> StudyGuidesChildComponent.Child.FolderChild(createStudyGuideComponent(componentContext, config.studyGuideLink))
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

    private val registered = MutableValue(false)
    private val backCallback = BackCallback { onBack.value() }
    override val onBack: MutableValue<() -> Unit> = MutableValue {}

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
    }
}

interface StudyGuideChildScreen