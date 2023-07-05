package nl.tiebe.otarium.logic.root.home.children.averages

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import dev.tiebe.magisterapi.response.general.year.grades.Subject
import nl.tiebe.otarium.logic.root.home.HomeComponent
import nl.tiebe.otarium.magister.GradeWithGradeInfo
import nl.tiebe.otarium.magister.ManualGrade

interface AveragesComponent : HomeComponent.MenuItemComponent {
    val refreshState: Value<Boolean>
    fun refreshGrades()

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

    val gradesList: Value<List<GradeWithGradeInfo>>
}