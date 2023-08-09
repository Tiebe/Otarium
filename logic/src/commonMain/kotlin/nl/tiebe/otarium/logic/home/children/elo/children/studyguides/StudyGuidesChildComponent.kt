package nl.tiebe.otarium.logic.home.children.elo.children.studyguides

import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import nl.tiebe.otarium.logic.home.children.elo.ELOComponent

interface StudyGuidesChildComponent<StudyGuide> : ELOComponent.ELOChildComponent {
    /** The stack navigation */
    val navigation: StackNavigation<Config>

    /**
     * Navigate to the given child.
     *
     * @param child The child to navigate to.
     */
    fun navigate(child: Config) {
        navigation.push(child)
    }

    /** The study guides. */
    val studyGuides: MutableValue<List<StudyGuide>>

    /**
     * The possible menus.
     */
    sealed class Config : Parcelable {
        /**
         * List of study guides.
         */
        @Parcelize
        data object StudyGuideList : Config()

        /**
         * The study guide.
         *
         * @param studyGuide The study guide.
         */
        @Parcelize
        data class StudyGuideMenu<StudyGuide>(val studyGuide: StudyGuide) : Config()
    }

    /** The interface that all the study guide child components should implement. */
    interface StudyGuideChildScreen
}
