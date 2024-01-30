package nl.tiebe.otarium.logic.root.home.children.averages

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import dev.tiebe.magisterapi.response.general.year.grades.Subject
import kotlinx.serialization.Serializable
import nl.tiebe.otarium.logic.root.home.HomeComponent
import nl.tiebe.otarium.magister.GradeWithGradeInfo
import nl.tiebe.otarium.magister.ManualGrade

interface AveragesComponent : HomeComponent.MenuItemComponent, BackHandlerOwner {
    val refreshState: Value<Boolean>
    fun refreshGrades()

    val navigation: StackNavigation<Config>
    val childStack: Value<ChildStack<Config, Child>>

    val manualGradesList: Value<List<ManualGrade>>
    val addManualGradePopupOpen: MutableValue<Boolean>

    fun addManualGrade(manualGrade: ManualGrade)
    fun removeManualGrade(manualGrade: ManualGrade)

    sealed class Child {
        class ListChild(val component: AveragesComponent) : Child()
        class SubjectChild(val component: AveragesComponent, val subjectId: Int) : Child()
    }

    @Serializable
    sealed class Config {
        @Serializable
        data object List : Config()

        @Serializable
        data class Subject(val subjectId: Int) : Config()
    }

    fun back()

    fun openSubject(subject: Subject) {
        navigation.push(Config.Subject(subject.id))
    }

    val gradesList: Value<List<GradeWithGradeInfo>>
    val cardList: MutableValue<Boolean>
}