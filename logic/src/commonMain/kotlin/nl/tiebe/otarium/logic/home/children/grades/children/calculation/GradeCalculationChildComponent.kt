package nl.tiebe.otarium.logic.home.children.grades.children.calculation

import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable
import nl.tiebe.otarium.logic.data.wrapper.Subject
import nl.tiebe.otarium.logic.home.children.grades.GradesComponent

/**
 * Interface for the implementation of the backend for the grade calculation UI.
 */
interface GradeCalculationChildComponent : GradesComponent.GradesChildComponent {
    /** The stack navigation */
    val navigation: StackNavigation<Config>

    /** The manual grades */
    val manualGradesList: Value<List<ManualGrade>>

    /**
     * Add a manual grade.
     *
     * @param manualGrade The manual grade to add.
     */
    fun addManualGrade(manualGrade: ManualGrade)

    /**
     * Remove a manual grade.
     *
     * @param manualGrade The manual grade to remove.
     */
    fun removeManualGrade(manualGrade: ManualGrade)

    /**
     * Navigate to the given subject.
     *
     * @param subject The subject to navigate to.
     */
    fun openSubject(subject: Subject) {
        navigation.push(Config.SubjectMenu(subject))
    }

    /**
     * Close the currently opened subject.
     */
    fun closeSubject() {
        navigation.pop()
    }

    /**
     * The possible menus.
     */
    sealed class Config : Parcelable {
        /**
         * The main menu.
         */
        @Parcelize
        data object Main : Config()

        /**
         * The subject menu.
         *
         * @param subject The subject to show the menu for.
         */
        @Parcelize
        data class SubjectMenu(val subject: Subject) : Config()
    }

    /**
     * A manually added grade.
     *
     * @param name The name of the grade.
     * @param grade The grade.
     * @param weight The weight of the grade.
     * @param subjectId The id of the subject.
     */
    @Serializable
    data class ManualGrade(
        val name: String,
        val grade: String,
        val weight: Float,
        val subjectId: Int,
    )
}