package nl.tiebe.otarium.logic.home.children.grades.children.recent

import com.arkivanov.decompose.value.MutableValue
import nl.tiebe.otarium.logic.home.children.grades.GradesComponent

/**
 * Interface for the recent grades menu of the grades screen.
 */
interface RecentGradesChildComponent<RecentGrade> : GradesComponent.GradesChildComponent {
    /** The currently loaded grades */
    val grades: MutableValue<List<RecentGrade>>

    /**
     * Refresh the grades.
     *
     * @return The new grades. These should also be saved to [grades].
     */
    fun refreshGrades(): List<RecentGrade>

    /**
     * Load the next page of grades. This should be done when the user scrolls to the bottom of the list.
     *
     * @return The new grades. These should also be saved to [grades].
     */
    fun loadNextGrades()

    /**
     * Calculate the average before and after the given grade.
     *
     * @param grade The grade to calculate the average before and after.
     * @return A pair of the average before and after the given grade.
     */
    fun calculateAverageBeforeAfter(grade: RecentGrade): Pair<Float, Float>


}