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
import nl.tiebe.otarium.magister.ManualGrade
import nl.tiebe.otarium.magister.refreshGrades
import nl.tiebe.otarium.ui.home.grades.GradesChildComponent
import nl.tiebe.otarium.ui.root.componentCoroutineScope

interface GradeCalculationChildComponent : GradesChildComponent {
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


fun calculateAverageGrade(grades: List<GradeWithGradeInfo>, addedGrade: Float = 0f, addedGradeWeight: Float = 0f): Float {
    return calculateAverage(grades.map {
        (it.grade.grade?.replace(',', '.')?.toFloatOrNull() ?: 0f) to it.gradeInfo.weight.toFloat()
    }, addedGrade, addedGradeWeight)
}

fun calculateAverage(pairs: List<Pair<Float, Float>>, initialAverage: Float = 0f, initialWeight: Float = 0f): Float {
    var sum = initialAverage * initialWeight
    var weight = initialWeight

    pairs.forEach {
        sum += it.first * it.second
        weight += it.second
    }

    if (weight == 0f) return 0f

    return sum/weight
}

fun calculateNewGrade(grades: List<GradeWithGradeInfo>, newAverage: Float = 10f, newGradeWeight: Float = 1f): Float {
    return calculateNew(grades.map {
        (it.grade.grade?.replace(',', '.')?.toFloatOrNull() ?: 0f) to it.gradeInfo.weight.toFloat()
    }, newAverage, newGradeWeight)
}

fun calculateNew(pairs: List<Pair<Float, Float>>, newAverage: Float = 10f, newGradeWeight: Float = 1f): Float {
    var sum = 0f
    var weight = newGradeWeight

    pairs.forEach {
        sum += it.first * it.second
        weight += it.second
    }

    return ((newAverage * weight) - sum) / newGradeWeight
}