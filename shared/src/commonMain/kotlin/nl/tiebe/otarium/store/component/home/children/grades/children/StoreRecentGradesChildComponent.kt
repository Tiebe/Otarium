package nl.tiebe.otarium.store.component.home.children.grades.children

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import dev.tiebe.magisterapi.response.general.year.grades.RecentGrade
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nl.tiebe.otarium.ui.home.grades.recentgrades.RecentGradesChildComponent
import nl.tiebe.otarium.ui.root.componentCoroutineScope

class StoreRecentGradesChildComponent(componentContext: ComponentContext) : RecentGradesChildComponent, ComponentContext by componentContext {
    override val refreshState: MutableValue<Boolean> = MutableValue(false)
    private val scope = componentCoroutineScope()

    override val grades: MutableValue<List<RecentGrade>> = MutableValue(listOf())

    override fun refreshGrades() {
        scope.launch {
            refreshState.value = true
            delay(1500)
            refreshState.value = false
        }
    }


    override fun loadNextGrades() {
        scope.launch {
            refreshState.value = true
            delay(1500)
            refreshState.value = false
        }
    }


    init {
        refreshGrades()
    }

}