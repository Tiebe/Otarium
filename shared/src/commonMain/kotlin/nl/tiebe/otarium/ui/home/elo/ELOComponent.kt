package nl.tiebe.otarium.ui.home.elo

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import nl.tiebe.otarium.ui.home.MenuItemComponent
import nl.tiebe.otarium.ui.home.elo.children.assignments.AssignmentsChildComponent
import nl.tiebe.otarium.ui.home.elo.children.assignments.DefaultAssignmentsChildComponent
import nl.tiebe.otarium.ui.home.elo.children.learningresources.DefaultLearningResourcesChildComponent
import nl.tiebe.otarium.ui.home.elo.children.learningresources.LearningResourcesChildComponent
import nl.tiebe.otarium.ui.home.elo.children.studyguides.DefaultStudyGuidesChildComponent
import nl.tiebe.otarium.ui.home.elo.children.studyguides.StudyGuidesChildComponent

interface ELOComponent : MenuItemComponent {
    @Parcelize
    sealed class ELOChild(val id: Int): Parcelable {
        object StudyGuides : ELOChild(0)
        object Assignments : ELOChild(1)
        object LearningResources : ELOChild(2)

    }

    val studyGuidesComponent: StudyGuidesChildComponent
    val assignmentsComponent: AssignmentsChildComponent
    val learningResourcesComponent: LearningResourcesChildComponent

    val currentPage: Value<Int>

    fun changeChild(eloChild: ELOChild)
}

class DefaultELOComponent(
    componentContext: ComponentContext
): ELOComponent, ComponentContext by componentContext {
    private fun studyGuidesComponent(componentContext: ComponentContext) =
        DefaultStudyGuidesChildComponent(
            componentContext = componentContext
        )

    private fun assignmentsComponent(componentContext: ComponentContext) =
        DefaultAssignmentsChildComponent(
            componentContext = componentContext
        )

    private fun learningResourcesComponent(componentContext: ComponentContext) =
        DefaultLearningResourcesChildComponent(
            componentContext = componentContext
        )

    override val studyGuidesComponent: StudyGuidesChildComponent = studyGuidesComponent(componentContext)
    override val assignmentsComponent: AssignmentsChildComponent = assignmentsComponent(componentContext)
    override val learningResourcesComponent: LearningResourcesChildComponent = learningResourcesComponent(componentContext)

    override val currentPage: MutableValue<Int> = MutableValue(0)


    override fun changeChild(eloChild: ELOComponent.ELOChild) {
        currentPage.value = eloChild.id
    }

}

interface ELOChildComponent