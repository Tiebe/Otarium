package nl.tiebe.otarium.ui.home.grades.recentgrades

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.response.general.year.grades.RecentGrade
import dev.tiebe.magisterapi.utils.MagisterException
import kotlinx.coroutines.launch
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.magister.getRecentGrades
import nl.tiebe.otarium.ui.home.grades.GradesChildComponent
import nl.tiebe.otarium.ui.root.componentCoroutineScope

interface RecentGradesChildComponent : GradesChildComponent {
    val refreshState: Value<Boolean>
    val grades: Value<List<RecentGrade>>

    fun refreshGrades()

}

class DefaultRecentGradesChildComponent(componentContext: ComponentContext) : RecentGradesChildComponent, ComponentContext by componentContext {
    override val refreshState: MutableValue<Boolean> = MutableValue(false)
    private val scope = componentCoroutineScope()

    override val grades: MutableValue<List<RecentGrade>> = MutableValue(Data.selectedAccount.grades)

    override fun refreshGrades() {
        scope.launch {
            refreshState.value = true
            try {
                grades.value = Data.selectedAccount.getRecentGrades()
            } catch (e: MagisterException) {
                e.printStackTrace()
            } catch (_: Exception) {
            }
            refreshState.value = false
        }
    }


    init {
        refreshGrades()
    }

}