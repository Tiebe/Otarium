package nl.tiebe.otarium.ui.home.grades

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.overlay.ChildOverlay
import com.arkivanov.decompose.router.overlay.OverlayNavigation
import com.arkivanov.decompose.router.overlay.activate
import com.arkivanov.decompose.router.overlay.childOverlay
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import nl.tiebe.otarium.ui.home.MenuItemComponent
import nl.tiebe.otarium.ui.home.grades.calculation.DefaultGradeCalculationChildComponent
import nl.tiebe.otarium.ui.home.grades.recentgrades.DefaultRecentGradesChildComponent

interface GradesComponent : MenuItemComponent {
    val dialog: Value<ChildOverlay<GradesChild, GradesChildComponent>>

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
    private val dialogNavigation = OverlayNavigation<GradesComponent.GradesChild>()


    override val dialog: Value<ChildOverlay<GradesComponent.GradesChild, GradesChildComponent>> = childOverlay(
        source = dialogNavigation,
        initialConfiguration = { GradesComponent.GradesChild.RecentGrades },
        // persistent = false, // Disable navigation state saving, if needed
        handleBackButton = false, // Close the dialog on back button press
    ) { config, componentContext ->
        when (config) {
            is GradesComponent.GradesChild.RecentGrades -> recentGradesComponent(componentContext)
            is GradesComponent.GradesChild.Calculation -> gradeCalculationComponent(componentContext)
        } as GradesChildComponent
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