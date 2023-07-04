package nl.tiebe.otarium.logic.default.home.children.grades

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import nl.tiebe.otarium.logic.default.home.children.grades.children.calculation.DefaultGradeCalculationChildComponent
import nl.tiebe.otarium.logic.default.home.children.grades.children.recent.DefaultGradesComponent
import nl.tiebe.otarium.logic.root.home.children.grades.children.calculation.GradeCalculationChildComponent

class DefaultGradesComponent(
    componentContext: ComponentContext
): nl.tiebe.otarium.logic.root.home.children.grades.GradesComponent, ComponentContext by componentContext {
    private fun recentGradesComponent(componentContext: ComponentContext) =
        DefaultGradesComponent(
            componentContext = componentContext
        )

    private fun gradeCalculationComponent(componentContext: ComponentContext) =
        DefaultGradeCalculationChildComponent(
            componentContext = componentContext
        )

    override val recentGradeComponent: nl.tiebe.otarium.logic.root.home.children.grades.children.recent.GradesComponent = recentGradesComponent(componentContext)
    override val calculationChildComponent: GradeCalculationChildComponent = gradeCalculationComponent(componentContext)

    override val currentPage: MutableValue<Int> = MutableValue(0)


    override fun changeChild(gradesChild: nl.tiebe.otarium.logic.root.home.children.grades.GradesComponent.GradesChild.GradesChild) {
        currentPage.value = gradesChild.id
    }

}