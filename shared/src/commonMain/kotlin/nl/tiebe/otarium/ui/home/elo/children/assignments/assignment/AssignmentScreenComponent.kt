package nl.tiebe.otarium.ui.home.elo.children.assignments.assignment

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.api.assignment.AssignmentFlow
import dev.tiebe.magisterapi.response.assignment.Assignment
import io.ktor.http.*
import kotlinx.coroutines.launch
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.ui.root.componentCoroutineScope

interface AssignmentScreenComponent {
    val assignment: Value<Assignment>
    val assignmentLink: String

    val isRefreshing: Value<Boolean>

    fun refreshAssignment()

}

class DefaultAssignmentScreenComponent(componentContext: ComponentContext, override val assignmentLink: String): AssignmentScreenComponent, ComponentContext by componentContext {
    override val assignment: MutableValue<Assignment> = MutableValue(Assignment(false, null, "", listOf(), null, 0, null, "", 0, listOf(), false, "", false, 0, "", "", listOf(), false))

    override val isRefreshing: MutableValue<Boolean> = MutableValue(false)
    val scope = componentCoroutineScope()

    override fun refreshAssignment() {
        scope.launch {
            isRefreshing.value = true
            assignment.value = AssignmentFlow.getFullAssignment(Url(Data.selectedAccount.tenantUrl), Data.selectedAccount.tokens.accessToken, assignmentLink)
            isRefreshing.value = false
        }
    }

    init {
        refreshAssignment()
    }


}