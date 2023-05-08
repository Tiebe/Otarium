package nl.tiebe.otarium.ui.home.elo.children.assignments

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.coroutines.CoroutineScope
import nl.tiebe.otarium.ui.home.elo.ELOChildComponent
import nl.tiebe.otarium.ui.home.elo.children.assignments.assignment.AssignmentScreenComponent
import nl.tiebe.otarium.ui.home.elo.children.assignments.assignment.DefaultAssignmentScreenComponent
import nl.tiebe.otarium.ui.home.elo.children.assignments.listscreen.AssignmentListComponent
import nl.tiebe.otarium.ui.home.elo.children.assignments.listscreen.DefaultAssignmentListComponent
import nl.tiebe.otarium.ui.root.componentCoroutineScope

interface AssignmentsChildComponent : ELOChildComponent {
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
}

class DefaultAssignmentsChildComponent(componentContext: ComponentContext) : AssignmentsChildComponent, ComponentContext by componentContext {
    override val refreshState: MutableValue<Boolean> = MutableValue(false)

    override val scope: CoroutineScope = componentCoroutineScope()

    override val navigation = StackNavigation<AssignmentsChildComponent.Config>()

    override val childStack: Value<ChildStack<AssignmentsChildComponent.Config, AssignmentsChildComponent.Child>> =
        childStack(
            source = navigation,
            initialConfiguration = AssignmentsChildComponent.Config.AssignmentList,
            key = "AssignmentsChildStack",
            handleBackButton = true, // Pop the back stack on back button press
            childFactory = ::createChild,
        )

    private fun createChild(config: AssignmentsChildComponent.Config, componentContext: ComponentContext): AssignmentsChildComponent.Child =
        when (config) {
            is AssignmentsChildComponent.Config.AssignmentList -> AssignmentsChildComponent.Child.AssignmentList(createAssignmentListComponent(this))
            is AssignmentsChildComponent.Config.Assignment -> AssignmentsChildComponent.Child.Assignment(createAssignmentScreenComponent(componentContext, config.assignmentLink))
        }

    private fun createAssignmentListComponent(componentContext: ComponentContext) =
        DefaultAssignmentListComponent(
            componentContext = componentContext,
            parentComponent = this
        )


    private fun createAssignmentScreenComponent(componentContext: ComponentContext, assignmentLink: String) =
        DefaultAssignmentScreenComponent(
            componentContext = componentContext,
            assignmentLink = assignmentLink,
        )
}

interface AssignmentChildScreen