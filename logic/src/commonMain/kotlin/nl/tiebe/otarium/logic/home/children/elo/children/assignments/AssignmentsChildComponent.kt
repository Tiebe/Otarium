package nl.tiebe.otarium.logic.home.children.elo.children.assignments

import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import nl.tiebe.otarium.logic.home.children.elo.ELOComponent


/**
 * The interface for the assignment child components.
 *
 * @param Assignment The type of assignment.
 */
interface AssignmentsChildComponent<Assignment> : ELOComponent.ELOChildComponent {
    /** The stack navigation. */
    val navigation: StackNavigation<Config>

    /**
     * Navigate to the given child.
     *
     * @param child The child to navigate to.
     */
    fun navigate(child: Config) {
        navigation.push(child)
    }

    /**
     * The possible submenus.
     */
    sealed class Config : Parcelable {
        /**
         * The list of assignments.
         */
        @Parcelize
        data object AssignmentList : Config()

        /**
         * The menu for an assignment.
         *
         * @param assignment The assignment to show the menu for.
         */
        @Parcelize
        data class AssignmentMenu<Assignment: Parcelable>(val assignment: Assignment) : Config()
    }

    /**
     * The interface that all the assignment child components should implement.
     */
    interface AssignmentChildScreen
}