package nl.tiebe.otarium.logic.store

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import dev.tiebe.magisterapi.response.general.year.grades.RecentGrade
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.logic.default.componentCoroutineScope
import nl.tiebe.otarium.logic.root.home.children.grades.RecentGradesComponent
import nl.tiebe.otarium.utils.calculateAverageGrade

class StoreRecentGradesComponent(componentContext: ComponentContext) : RecentGradesComponent, ComponentContext by componentContext {
    override val refreshState: MutableValue<Boolean> = MutableValue(false)
    private val scope = componentCoroutineScope()

    override val grades: MutableValue<List<RecentGrade>> = MutableValue(emptyList())

    override fun refreshGrades() {
        scope.launch {
            refreshState.value = true
            delay(1000L)
            refreshState.value = false
        }
    }


    override fun loadNextGrades() {
        scope.launch {
            refreshState.value = true
            delay(700L)
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