package dev.tiebe.otarium.store.component.home.children.grades

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import dev.tiebe.otarium.store.component.home.children.grades.children.StoreGradeCalculationChildComponent
import dev.tiebe.otarium.store.component.home.children.grades.children.StoreRecentGradesChildComponent
import dev.tiebe.otarium.ui.home.grades.GradesComponent
import dev.tiebe.otarium.ui.home.grades.calculation.GradeCalculationChildComponent
import dev.tiebe.otarium.ui.home.grades.recentgrades.RecentGradesChildComponent

class StoreGradeComponent(
    componentContext: ComponentContext
): GradesComponent, ComponentContext by componentContext {
    private fun recentGradesComponent(componentContext: ComponentContext) =
        StoreRecentGradesChildComponent(
            componentContext = componentContext
        )

    private fun gradeCalculationComponent(componentContext: ComponentContext) =
        StoreGradeCalculationChildComponent(
            componentContext = componentContext
        )

    override val recentGradeComponent: RecentGradesChildComponent = recentGradesComponent(componentContext)
    override val calculationChildComponent: GradeCalculationChildComponent = gradeCalculationComponent(componentContext)

    override val currentPage: MutableValue<Int> = MutableValue(0)


    override fun changeChild(gradesChild: GradesComponent.GradesChild) {
        currentPage.value = gradesChild.id
    }

}