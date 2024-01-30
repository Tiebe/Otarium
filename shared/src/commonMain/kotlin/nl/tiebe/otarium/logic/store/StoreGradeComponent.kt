package nl.tiebe.otarium.logic.store

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import nl.tiebe.otarium.logic.default.home.children.averages.DefaultAveragesComponent
import nl.tiebe.otarium.logic.default.home.children.grades.DefaultRecentGradesComponent
import nl.tiebe.otarium.logic.root.home.children.averages.AveragesComponent
import nl.tiebe.otarium.logic.root.home.children.grades.RecentGradesComponent
import nl.tiebe.otarium.ui.home.grades.GradesComponent

class StoreGradeComponent(componentContext: ComponentContext): GradesComponent, ComponentContext by componentContext {
    private fun recentGradesComponent(componentContext: ComponentContext) =
        StoreRecentGradesComponent(
            componentContext = componentContext
        )

    private fun averagesComponent(componentContext: ComponentContext) =
        StoreAveragesComponent(
            componentContext = componentContext
        )

    override val recentGradeComponent: RecentGradesComponent = recentGradesComponent(componentContext)
    override val averagesComponent: AveragesComponent = averagesComponent(componentContext)

    override val currentPage: MutableValue<Int> = MutableValue(0)

    override fun changeChild(gradesChild: GradesComponent.GradesChild) {
        currentPage.value = gradesChild.id
    }

}