package dev.tiebe.otarium.store.component.home.children.grades.children

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import dev.tiebe.magisterapi.response.general.year.grades.RecentGrade
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import dev.tiebe.otarium.MR
import dev.tiebe.otarium.logic.home.children.grades.children.recent.RecentGradesChildComponent
import dev.tiebe.otarium.logic.default.componentCoroutineScope
import dev.tiebe.otarium.utils.ui.getText

class StoreRecentGradesChildComponent(componentContext: ComponentContext) : RecentGradesChildComponent, ComponentContext by componentContext {
    override val refreshState: MutableValue<Boolean> = MutableValue(false)
    private val scope = componentCoroutineScope()

    override val grades: MutableValue<List<RecentGrade>> = MutableValue(listOf())

    override fun refreshGrades() {
        scope.launch {
            refreshState.value = true
            delay(1500)
            grades.value = Json.decodeFromString(getText(MR.files.recentgrades))
            refreshState.value = false
        }
    }


    override fun loadNextGrades() {
        scope.launch {
            refreshState.value = true
            delay(200)
            refreshState.value = false
        }
    }

    override fun calculateAverageBeforeAfter(grade: RecentGrade): Pair<Float, Float> {
        return (grade.grade.replace(",", ".").toFloatOrNull() ?: 0f) to (grade.grade.replace(",", ".").toFloatOrNull() ?: 0f)
    }


    init {
        refreshGrades()
    }

}