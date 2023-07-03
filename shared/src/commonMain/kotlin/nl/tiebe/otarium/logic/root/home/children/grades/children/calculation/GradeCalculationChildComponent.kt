package nl.tiebe.otarium.logic.root.home.children.grades.children.calculation

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import dev.tiebe.magisterapi.response.general.year.grades.Subject
import nl.tiebe.otarium.logic.root.home.children.grades.GradesComponent
import nl.tiebe.otarium.magister.GradeWithGradeInfo
import nl.tiebe.otarium.magister.ManualGrade

interface GradeCalculationChildComponent : GradesComponent.GradesChildComponent {
    val openedSubject: Value<Pair<Boolean, Subject?>>

    val backCallbackOpenItem: BackCallback

    val manualGradesList: Value<List<ManualGrade>>
    val addManualGradePopupOpen: MutableValue<Boolean>

    fun addManualGrade(manualGrade: ManualGrade)
    fun removeManualGrade(manualGrade: ManualGrade)

    fun openSubject(subject: Subject) {
        backCallbackOpenItem.isEnabled = true

        (openedSubject as MutableValue).value = true to subject
    }

    fun closeSubject() {
        backCallbackOpenItem.isEnabled = false
        (openedSubject as MutableValue).value = false to openedSubject.value.second
    }

    sealed class State {
        object Loading: State()
        data class Data(val data: List<GradeWithGradeInfo>): State()
        object Failed: State()
    }

    val state: Value<State>
}