package nl.tiebe.otarium.logic.home.children.elo

import com.arkivanov.essenty.parcelable.Parcelable
import nl.tiebe.otarium.logic.home.HomeComponent
import nl.tiebe.otarium.logic.home.children.elo.children.assignments.AssignmentsChildComponent
import nl.tiebe.otarium.logic.home.children.elo.children.learningresources.LearningResourcesChildComponent
import nl.tiebe.otarium.logic.home.children.elo.children.studyguides.StudyGuidesChildComponent

/**
 * The ELOComponent is the component that contains all the ELO related components.
 * This component is used to navigate to the different ELO related components.
 *
 * @param StudyGuide The type of a study guide.
 * @param Assignment The type of a assignment.
 * @param LearningResource The type of a learning resource.
 */
interface ELOComponent : HomeComponent.MenuItemComponent {
    /** The StudyGuidesChildComponent is the component that contains all the study guides. */
    val studyGuidesComponent: StudyGuidesChildComponent
    /** The AssignmentsChildComponent is the component that contains all the assignments. */
    val assignmentsComponent: AssignmentsChildComponent
    /** The LearningResourcesChildComponent is the component that contains all the learning resources. */
    val learningResourcesComponent: LearningResourcesChildComponent

    /** The interface that all the ELO child components should implement. */
    interface ELOChildComponent
}
