package dev.tiebe.otarium.ui.home.grades.recentgrades

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.response.general.year.grades.RecentGrade
import dev.tiebe.magisterapi.utils.MagisterException
import kotlinx.coroutines.launch
import dev.tiebe.otarium.Data
import dev.tiebe.otarium.magister.getRecentGrades
import dev.tiebe.otarium.ui.home.grades.GradesChildComponent
import dev.tiebe.otarium.ui.home.grades.calculation.calculateAverageGrade
import dev.tiebe.otarium.ui.root.componentCoroutineScope

interface RecentGradesChildComponent : GradesChildComponent {
    val refreshState: Value<Boolean>
    val grades: Value<List<RecentGrade>>

    fun refreshGrades()

    fun loadNextGrades()

    fun calculateAverageBeforeAfter(grade: RecentGrade): Pair<Float, Float>


}

class DefaultRecentGradesChildComponent(componentContext: ComponentContext) : RecentGradesChildComponent, ComponentContext by componentContext {
    override val refreshState: MutableValue<Boolean> = MutableValue(false)
    private val scope = componentCoroutineScope()

    override val grades: MutableValue<List<RecentGrade>> = MutableValue(Data.selectedAccount.grades)

    override fun refreshGrades() {
        scope.launch {
            refreshState.value = true
            try {
                grades.value = Data.selectedAccount.getRecentGrades(30, 0)
            } catch (e: MagisterException) {
                e.printStackTrace()
            } catch (_: Exception) {
            }
            refreshState.value = false
        }
    }


    override fun loadNextGrades() {
        scope.launch {
            refreshState.value = true
            try {
                grades.value = listOf(grades.value, Data.selectedAccount.getRecentGrades(30, grades.value.size)).flatten()
            } catch (e: MagisterException) {
                e.printStackTrace()
            } catch (_: Exception) {
            }
            refreshState.value = false
        }
    }

    override fun calculateAverageBeforeAfter(grade: RecentGrade): Pair<Float, Float> {
        val grades = Data.selectedAccount.fullGradeList.filter { it.grade.subject.abbreviation == grade.subject.code }.sortedBy { it.grade.dateEntered }

        val index = grades.find { it.grade.gradeColumn.id == grade.gradeColumnId }?.let { grades.indexOf(it) } ?: return 0f to 0f

        val before = calculateAverageGrade(grades.subList(0, index))
        val after = calculateAverageGrade(grades.subList(0, index + 1))

        return before to after
    }


    init {
        refreshGrades()
    }

}