package nl.tiebe.otarium.logic.default.home.children.averages

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.backhandler.BackCallback
import dev.tiebe.magisterapi.response.general.year.grades.GradeColumn
import dev.tiebe.magisterapi.response.general.year.grades.Subject
import kotlinx.coroutines.launch
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.logic.default.componentCoroutineScope
import nl.tiebe.otarium.logic.root.home.children.averages.AveragesComponent
import nl.tiebe.otarium.magister.GradeWithGradeInfo
import nl.tiebe.otarium.magister.ManualGrade

class DefaultAveragesComponent(componentContext: ComponentContext) : AveragesComponent, ComponentContext by componentContext {
    override val refreshState: MutableValue<Boolean> = MutableValue(false)

    override fun refreshGrades() {
        scope.launch {
            refreshState.value = true
            //TODO: Data.selectedAccount.fullGradeList = Data.selectedAccount.refreshGrades()
            gradesList.value = Data.selectedAccount.fullGradeList.filter {
                it.grade.gradeColumn.type == GradeColumn.Type.Grade &&
                        it.grade.grade?.replace(",", ".")?.toDoubleOrNull() != null
            }
            refreshState.value = false
        }
    }

    override val openedSubject: MutableValue<Pair<Boolean, Subject?>> = MutableValue(false to null)
    override val backCallbackOpenItem = BackCallback(false) {
        closeSubject()
    }

    override val gradesList: MutableValue<List<GradeWithGradeInfo>> = MutableValue(
        Data.selectedAccount.fullGradeList.filter {
            it.grade.gradeColumn.type == GradeColumn.Type.Grade &&
                    it.grade.grade?.replace(",", ".")?.toDoubleOrNull() != null
        }
    )

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

    private val scope = componentCoroutineScope()

    init {
        backHandler.register(backCallbackOpenItem)

        refreshGrades()
    }
}