package nl.tiebe.otarium.logic.home.children.elo.children.studyguides.children.list

import com.arkivanov.decompose.value.Value
import nl.tiebe.otarium.logic.data.wrapper.elo.StudyGuide
import nl.tiebe.otarium.logic.home.children.elo.children.studyguides.StudyGuidesChildComponent

/**
 * A child component of the study guides component that handles the study guide list.
 *
 * @param StudyGuide The type of study guide.
 */
interface StudyGuideListComponent : StudyGuidesChildComponent.StudyGuideChildScreen {
    /** The study guides. */
    val studyGuides: Value<List<StudyGuide>>

    /** The parent component. */
    val parentComponent: StudyGuidesChildComponent

    /**
     * Refreshes the study guides.
     *
     * @return The refreshed study guides. These should also be stored in [studyGuides].
     */
    fun refreshStudyGuides(): List<StudyGuide>

    /**
     * Navigates to the study guide menu.
     *
     * @param studyGuide The study guide to navigate to.
     */
    fun navigateToStudyGuide(studyGuide: StudyGuide) {
        parentComponent.navigate(StudyGuidesChildComponent.Config.StudyGuideMenu(studyGuide))
    }
}