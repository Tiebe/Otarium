package nl.tiebe.otarium.logic.root.home.children.grades

import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.response.general.year.grades.RecentGrade
import nl.tiebe.otarium.logic.root.home.HomeComponent

interface RecentGradesComponent : HomeComponent.MenuItemComponent {
    val refreshState: Value<Boolean>
    val grades: Value<List<RecentGrade>>

    fun refreshGrades()

    fun loadNextGrades()

    fun calculateAverageBeforeAfter(grade: RecentGrade): Pair<Float, Float>


}