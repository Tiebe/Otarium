package nl.tiebe.otarium.logic.home.children.elo.children.assignments.children.list

import com.arkivanov.decompose.value.Value
import nl.tiebe.otarium.logic.data.wrapper.elo.Assignment
import nl.tiebe.otarium.logic.home.children.elo.children.assignments.AssignmentsChildComponent

/**
 * The component for the assignment list.
 *
 * @param Assignment The type of assignment.
 */
interface AssignmentListComponent : AssignmentsChildComponent.AssignmentChildScreen {
    /** The assignments. */
    val assignments: Value<List<Assignment>>
    /** The parent component. */
    val parentComponent: AssignmentsChildComponent

    /**
     * Refresh the assignments.
     *
     * @return The refreshed assignments. This should also be set in the [assignments] value.
     */
    fun refreshAssignments(): List<Assignment>

    /**
     * Navigate to the given assignment.
     *
     * @param assignment The assignment to navigate to.
     */
    fun navigateToAssignment(assignment: Assignment) {
        parentComponent.navigate(AssignmentsChildComponent.Config.AssignmentMenu(assignment))
    }
}