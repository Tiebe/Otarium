package nl.tiebe.otarium.logic.root.home.children.grades.children.recent

import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.response.general.year.grades.RecentGrade
import nl.tiebe.otarium.logic.root.home.children.grades.GradesComponent

interface RecentGradesChildComponent : GradesComponent.GradesChildComponent {
    val refreshState: Value<Boolean>
    val grades: Value<List<RecentGrade>>

    fun refreshGrades()

    fun loadNextGrades()

    fun calculateAverageBeforeAfter(grade: RecentGrade): Pair<Float, Float>


}