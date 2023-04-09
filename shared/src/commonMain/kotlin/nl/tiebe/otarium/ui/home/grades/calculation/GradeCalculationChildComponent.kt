package nl.tiebe.otarium.ui.home.grades.calculation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import dev.tiebe.magisterapi.response.general.year.grades.GradeColumn
import dev.tiebe.magisterapi.response.general.year.grades.Subject
import kotlinx.coroutines.launch
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.magister.GradeWithGradeInfo
import nl.tiebe.otarium.magister.refreshGrades
import nl.tiebe.otarium.ui.home.grades.GradesChildComponent
import nl.tiebe.otarium.ui.root.componentCoroutineScope

//todo: more component shit?

interface GradeCalculationChildComponent : GradesChildComponent {
    val openedSubject: Value<Pair<Boolean, Subject?>>

    val backCallbackOpenItem: BackCallback

    fun openSubject(subject: Subject) {
        backCallbackOpenItem.isEnabled = true

        (openedSubject as MutableValue).value = true to subject
    }

    sealed class State {
        object Loading: State()
        data class Data(val data: List<GradeWithGradeInfo>): State()
        object Failed: State()
    }

    val state: Value<State>
}

class DefaultGradeCalculationChildComponent(componentContext: ComponentContext) : GradeCalculationChildComponent, ComponentContext by componentContext {
    override val openedSubject: MutableValue<Pair<Boolean, Subject?>> = MutableValue(false to null)
    override val backCallbackOpenItem = BackCallback(false) {
        closeSubject()
    }

    private fun closeSubject() {
        backCallbackOpenItem.isEnabled = false
        openedSubject.value = false to openedSubject.value.second
    }

    override val state: MutableValue<GradeCalculationChildComponent.State> = MutableValue(GradeCalculationChildComponent.State.Loading)
    private val scope = componentCoroutineScope()

    init {
        backHandler.register(backCallbackOpenItem)

        scope.launch {
            try {
                state.value = GradeCalculationChildComponent.State.Data(Data.selectedAccount.refreshGrades().filter {
                    it.grade.gradeColumn.type == GradeColumn.Type.Grade &&
                            it.grade.grade?.replace(",", ".")?.toDoubleOrNull() != null
                })
                return@launch
            } catch (e: Exception) {
                e.printStackTrace()
            }
            state.value = GradeCalculationChildComponent.State.Failed
        }
    }



}