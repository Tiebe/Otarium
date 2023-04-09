package nl.tiebe.otarium.ui.home.grades.recentgrades

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
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

    val openedGrade: Value<Pair<Boolean, RecentGrade?>>

    fun refreshGrades()

    val backCallbackOpenItem: BackCallback

    fun openRecentGrade(item: RecentGrade) {
        backCallbackOpenItem.isEnabled = true

        (openedGrade as MutableValue).value = true to item
    }

    fun closeRecentGrade() {
        (openedGrade as MutableValue).value = false to openedGrade.value.second
        backCallbackOpenItem.isEnabled = false
    }

    fun loadNextGrades()



}

class DefaultRecentGradesChildComponent(componentContext: ComponentContext) : RecentGradesChildComponent, ComponentContext by componentContext {
    override val refreshState: MutableValue<Boolean> = MutableValue(false)
    private val scope = componentCoroutineScope()

    override val grades: MutableValue<List<RecentGrade>> = MutableValue(Data.selectedAccount.grades)
    override val openedGrade = MutableValue<Pair<Boolean, RecentGrade?>>(false to null)

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



    override val backCallbackOpenItem = BackCallback(false) {
        closeRecentGrade()
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


    init {
        backHandler.register(backCallbackOpenItem)

        refreshGrades()
    }

}