package nl.tiebe.otarium.logic.home.children.grades

import com.arkivanov.essenty.parcelable.Parcelable
import nl.tiebe.otarium.logic.home.HomeComponent
import nl.tiebe.otarium.logic.home.children.grades.children.calculation.GradeCalculationChildComponent
import nl.tiebe.otarium.logic.home.children.grades.children.recent.RecentGradesChildComponent


/**
 * Interface for the grades menu of the home screen.
 */
interface GradesComponent : HomeComponent.MenuItemComponent {
    /** The recent grades menu. */
    val recentGradeComponent: RecentGradesChildComponent

    /** The grade calculation menu. */
    val calculationChildComponent: GradeCalculationChildComponent

    /** The interface for submenus. */
    interface GradesChildComponent
}