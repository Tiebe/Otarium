package nl.tiebe.otarium.ui.home.grades

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import nl.tiebe.otarium.ui.home.MenuItemComponent
import nl.tiebe.otarium.ui.home.grades.calculation.DefaultGradeCalculationChildComponent
import nl.tiebe.otarium.ui.home.grades.recentgrades.DefaultRecentGradesChildComponent

interface GradesComponent : MenuItemComponent {
    val dialog: Value<ChildSlot<GradesChild, GradesChildComponent>>

    @Parcelize
    sealed class GradesChild(val id: Int): Parcelable {
        object RecentGrades : GradesChild(0)
        object Calculation : GradesChild(1)

    }

    fun changeChild(gradesChild: GradesChild)
}

class DefaultGradesComponent(
    componentContext: ComponentContext
): GradesComponent, ComponentContext by componentContext {
    private val dialogNavigation = SlotNavigation<GradesComponent.GradesChild>()


    override val dialog: Value<ChildSlot<GradesComponent.GradesChild, GradesChildComponent>> = childSlot<GradesComponent.GradesChild, GradesChildComponent>(
            dialogNavigation,
            "DefaultChildOverlay", { GradesComponent.GradesChild.RecentGrades },
            persistent = true,
            handleBackButton = false
        ) { config, componentContext ->
            when (config) {
                is GradesComponent.GradesChild.RecentGrades -> recentGradesComponent(componentContext)
                is GradesComponent.GradesChild.Calculation -> gradeCalculationComponent(componentContext)
            }
        }

    private fun recentGradesComponent(componentContext: ComponentContext) =
        DefaultRecentGradesChildComponent(
            componentContext = componentContext
        )

    private fun gradeCalculationComponent(componentContext: ComponentContext) =
        DefaultGradeCalculationChildComponent(
            componentContext = componentContext
        )



    override fun changeChild(gradesChild: GradesComponent.GradesChild) {
        dialogNavigation.activate(gradesChild)
    }

}

interface GradesChildComponent {
}