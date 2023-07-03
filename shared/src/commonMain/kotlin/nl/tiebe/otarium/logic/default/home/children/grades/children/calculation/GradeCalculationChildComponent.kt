package nl.tiebe.otarium.logic.default.home.children.grades.children.calculation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.backhandler.BackCallback
import dev.tiebe.magisterapi.response.general.year.grades.GradeColumn
import dev.tiebe.magisterapi.response.general.year.grades.Subject
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.logic.default.componentCoroutineScope
import nl.tiebe.otarium.logic.root.home.children.grades.children.calculation.GradeCalculationChildComponent
import nl.tiebe.otarium.magister.ManualGrade
import nl.tiebe.otarium.magister.refreshGrades
import kotlinx.coroutines.launch

class DefaultGradeCalculationChildComponent(componentContext: ComponentContext) : GradeCalculationChildComponent, ComponentContext by componentContext {
    override val openedSubject: MutableValue<Pair<Boolean, Subject?>> = MutableValue(false to null)
    override val backCallbackOpenItem = BackCallback(false) {
        closeSubject()
    }
    override val manualGradesList: MutableValue<List<ManualGrade>> = MutableValue(Data.manualGrades)
    override val addManualGradePopupOpen: MutableValue<Boolean> = MutableValue(false)
    override fun addManualGrade(manualGrade: ManualGrade) {
        manualGradesList.value = manualGradesList.value.toMutableList().apply {
            add(manualGrade)
        }
        Data.manualGrades = manualGradesList.value
    }

    override fun removeManualGrade(manualGrade: ManualGrade) {
        manualGradesList.value = manualGradesList.value.toMutableList().apply {
            remove(manualGrade)
        }
        Data.manualGrades = manualGradesList.value
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