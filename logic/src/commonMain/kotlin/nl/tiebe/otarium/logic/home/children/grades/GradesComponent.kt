package nl.tiebe.otarium.logic.home.children.grades

import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import nl.tiebe.otarium.logic.root.home.HomeComponent
import nl.tiebe.otarium.logic.root.home.children.grades.children.calculation.GradeCalculationChildComponent
import nl.tiebe.otarium.logic.root.home.children.grades.children.recent.RecentGradesChildComponent

interface GradesComponent : HomeComponent.MenuItemComponent {
    @Parcelize
    sealed class GradesChild(val id: Int): Parcelable {
        object RecentGrades : GradesChild(0)
        object Calculation : GradesChild(1)

        //todo: overzicht van alle cijfers

    }

    val recentGradeComponent: RecentGradesChildComponent
    val calculationChildComponent: GradeCalculationChildComponent

    val currentPage: Value<Int>

    fun changeChild(gradesChild: GradesChild)

    interface GradesChildComponent
}