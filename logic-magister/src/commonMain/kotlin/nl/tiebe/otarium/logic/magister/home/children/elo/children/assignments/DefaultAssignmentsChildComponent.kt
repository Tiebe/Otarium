package nl.tiebe.otarium.logic.default.home.children.elo.children.assignments

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import kotlinx.coroutines.CoroutineScope
import nl.tiebe.otarium.logic.default.componentCoroutineScope
import nl.tiebe.otarium.logic.default.home.children.elo.children.assignments.children.assignment.DefaultAssignmentScreenComponent
import nl.tiebe.otarium.logic.root.home.children.elo.children.assignments.AssignmentsChildComponent
import nl.tiebe.otarium.logic.default.home.children.elo.children.assignments.children.list.DefaultAssignmentListComponent

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

    private fun createChild(
        config: AssignmentsChildComponent.Config,
        componentContext: ComponentContext
    ): AssignmentsChildComponent.Child =
        when (config) {
            is AssignmentsChildComponent.Config.AssignmentList -> AssignmentsChildComponent.Child.AssignmentList(
                createAssignmentListComponent(this)
            )

            is AssignmentsChildComponent.Config.Assignment -> AssignmentsChildComponent.Child.Assignment(
                createAssignmentScreenComponent(componentContext, config.assignmentLink)
            )
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

    private val registered = MutableValue(false)
    private val backCallback = BackCallback { onBack.value() }
    override val onBack: MutableValue<() -> Unit> = MutableValue {}

    override fun registerBackHandler() {
        if (registered.value) return
        backHandler.register(backCallback)
        registered.value = true
    }

    override fun unregisterBackHandler() {
        if (!registered.value) return
        backHandler.unregister(backCallback)
        registered.value = false
    }


    init {
        childStack.subscribe { childStack ->
            if (childStack.backStack.isEmpty()) {
                unregisterBackHandler()
            } else {
                registerBackHandler()
            }
        }
    }
}