package dev.tiebe.otarium.store.component.home.children.grades.children

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.backhandler.BackCallback
import dev.tiebe.magisterapi.response.general.year.grades.Subject
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import dev.tiebe.otarium.Data
import dev.tiebe.otarium.MR
import dev.tiebe.otarium.magister.ManualGrade
import dev.tiebe.otarium.logic.home.children.grades.children.calculation.GradeCalculationChildComponent
import dev.tiebe.otarium.logic.default.componentCoroutineScope
import dev.tiebe.otarium.utils.ui.getText

class StoreGradeCalculationChildComponent(componentContext: ComponentContext
) : GradeCalculationChildComponent, ComponentContext by componentContext {
    override val openedSubject: MutableValue<Pair<Boolean, Subject?>> = MutableValue(false to null)
    override val backCallbackOpenItem = BackCallback(false) {
        closeSubject()
    }

    override val manualGradesList: MutableValue<MutableList<ManualGrade>> = MutableValue(Data.manualGrades.toMutableList())
    override val addManualGradePopupOpen: MutableValue<Boolean> = MutableValue(false)

    override fun addManualGrade(manualGrade: ManualGrade) {
        manualGradesList.value.add(manualGrade)
        Data.manualGrades = manualGradesList.value
    }

    override fun removeManualGrade(manualGrade: ManualGrade) {
        manualGradesList.value.remove(manualGrade)
        Data.manualGrades = manualGradesList.value
    }

    override val state: MutableValue<GradeCalculationChildComponent.State> =
        MutableValue(GradeCalculationChildComponent.State.Loading)
    private val scope = componentCoroutineScope()

    init {
        backHandler.register(backCallbackOpenItem)

        scope.launch {
            state.value = GradeCalculationChildComponent.State.Data(Json.decodeFromString(getText(MR.files.averages)))
        }
    }
}