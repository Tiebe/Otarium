package dev.tiebe.otarium.logic.default.home.children.grades

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import dev.tiebe.otarium.logic.default.home.MenuItemComponent
import dev.tiebe.otarium.logic.home.children.grades.children.calculation.DefaultGradeCalculationChildComponent
import dev.tiebe.otarium.logic.home.children.grades.children.calculation.GradeCalculationChildComponent
import dev.tiebe.otarium.logic.home.children.grades.children.recent.DefaultRecentGradesChildComponent
import dev.tiebe.otarium.logic.home.children.grades.children.recent.RecentGradesChildComponent

class DefaultGradesComponent(
    componentContext: ComponentContext
): GradesComponent, ComponentContext by componentContext {
    private fun recentGradesComponent(componentContext: ComponentContext) =
        DefaultRecentGradesChildComponent(
            componentContext = componentContext
        )

    private fun gradeCalculationComponent(componentContext: ComponentContext) =
        DefaultGradeCalculationChildComponent(
            componentContext = componentContext
        )

    override val recentGradeComponent: RecentGradesChildComponent = recentGradesComponent(componentContext)
    override val calculationChildComponent: GradeCalculationChildComponent = gradeCalculationComponent(componentContext)

    override val currentPage: MutableValue<Int> = MutableValue(0)


    override fun changeChild(gradesChild: GradesComponent.GradesChild) {
        currentPage.value = gradesChild.id
    }

}

interface GradesChildComponent {
}