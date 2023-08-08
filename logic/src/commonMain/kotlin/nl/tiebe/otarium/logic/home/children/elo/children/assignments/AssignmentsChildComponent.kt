package nl.tiebe.otarium.logic.home.children.elo.children.assignments

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import nl.tiebe.otarium.logic.root.home.children.elo.ELOComponent
import nl.tiebe.otarium.logic.root.home.children.elo.children.assignments.children.assignment.AssignmentScreenComponent
import nl.tiebe.otarium.logic.root.home.children.elo.children.assignments.children.list.AssignmentListComponent
import kotlinx.coroutines.CoroutineScope

interface AssignmentsChildComponent : ELOComponent.ELOChildComponent {
    val navigation: StackNavigation<Config>
    val childStack: Value<ChildStack<Config, Child>>

    val refreshState: Value<Boolean>
    val scope: CoroutineScope

    fun navigate(child: Config) {
        navigation.push(child)
    }

    sealed class Child {
        class AssignmentList(val component: AssignmentListComponent) : Child()
        class Assignment(val component: AssignmentScreenComponent) : Child()
    }

    sealed class Config : Parcelable {
        @Parcelize
        object AssignmentList : Config()

        @Parcelize
        data class Assignment(val assignmentLink: String) : Config()
    }


    val onBack: MutableValue<() -> Unit>

    fun registerBackHandler()
    fun unregisterBackHandler()

    interface AssignmentChildScreen
}