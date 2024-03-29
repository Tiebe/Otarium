package nl.tiebe.otarium.logic.root.home.children.elo

import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import nl.tiebe.otarium.logic.root.home.HomeComponent
import nl.tiebe.otarium.logic.root.home.children.elo.children.assignments.AssignmentsChildComponent
import nl.tiebe.otarium.logic.root.home.children.elo.children.learningresources.LearningResourcesChildComponent
import nl.tiebe.otarium.logic.root.home.children.elo.children.studyguides.StudyGuidesChildComponent

interface ELOComponent : HomeComponent.MenuItemComponent {
    sealed class ELOChild(val id: Int) {
        data object StudyGuides : ELOChild(0)
        data object Assignments : ELOChild(1)
        data object LearningResources : ELOChild(2)

    }

    val studyGuidesComponent: StudyGuidesChildComponent
    val assignmentsComponent: AssignmentsChildComponent
    val learningResourcesComponent: LearningResourcesChildComponent

    val currentPage: Value<Int>

    fun changeChild(eloChild: ELOChild)

    interface ELOChildComponent
}
