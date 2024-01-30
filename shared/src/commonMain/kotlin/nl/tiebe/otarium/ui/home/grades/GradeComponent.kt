package nl.tiebe.otarium.ui.home.grades

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import kotlinx.serialization.Serializable
import nl.tiebe.otarium.logic.default.home.children.averages.DefaultAveragesComponent
import nl.tiebe.otarium.logic.default.home.children.grades.DefaultRecentGradesComponent
import nl.tiebe.otarium.logic.root.home.HomeComponent
import nl.tiebe.otarium.logic.root.home.children.averages.AveragesComponent
import nl.tiebe.otarium.logic.root.home.children.grades.RecentGradesComponent

interface GradesComponent : HomeComponent.MenuItemComponent {
    @Serializable
    sealed class GradesChild(val id: Int): Parcelable {
        @Serializable
        data object RecentGrades : GradesChild(0)
        @Serializable
        data object Calculation : GradesChild(1)

    }

    val recentGradeComponent: RecentGradesComponent
    val averagesComponent: AveragesComponent

    val currentPage: Value<Int>

    fun changeChild(gradesChild: GradesChild)
}

class DefaultGradesComponent(
    componentContext: ComponentContext
): GradesComponent, ComponentContext by componentContext {
    private fun recentGradesComponent(componentContext: ComponentContext) =
        DefaultRecentGradesComponent(
            componentContext = componentContext
        )

    private fun averagesComponent(componentContext: ComponentContext) =
        DefaultAveragesComponent(
            componentContext = componentContext
        )

    override val recentGradeComponent: RecentGradesComponent = recentGradesComponent(componentContext)
    override val averagesComponent: AveragesComponent = averagesComponent(componentContext)

    override val currentPage: MutableValue<Int> = MutableValue(0)

    override fun changeChild(gradesChild: GradesComponent.GradesChild) {
        currentPage.value = gradesChild.id
    }

}