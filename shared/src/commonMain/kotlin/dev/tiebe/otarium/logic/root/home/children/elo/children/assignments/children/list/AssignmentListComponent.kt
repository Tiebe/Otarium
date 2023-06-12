package dev.tiebe.otarium.logic.root.home.children.elo.children.assignments.children.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.api.assignment.AssignmentFlow
import dev.tiebe.magisterapi.api.general.GeneralFlow
import dev.tiebe.magisterapi.response.assignment.Assignment
import dev.tiebe.otarium.Data
import dev.tiebe.otarium.logic.default.componentCoroutineScope
import dev.tiebe.otarium.logic.root.home.children.elo.children.assignments.AssignmentsChildComponent
import io.ktor.http.*
import kotlinx.coroutines.launch

interface AssignmentListComponent : AssignmentsChildComponent.AssignmentChildScreen {
    val assignments: Value<List<Assignment>>
    val parentComponent: AssignmentsChildComponent

    val isRefreshing: Value<Boolean>

    fun refreshAssignments()

    fun navigateToAssignment(assignment: Assignment) {
        parentComponent.navigate(AssignmentsChildComponent.Config.Assignment(assignment.links.first { it.rel == "Self" }.href))
    }

    val selectedTab: MutableValue<Int>
}

class DefaultAssignmentListComponent(
    componentContext: ComponentContext,
    override val parentComponent: AssignmentsChildComponent
) : AssignmentListComponent, ComponentContext by componentContext {
    override val assignments: MutableValue<List<Assignment>> = MutableValue(listOf())

    override val isRefreshing: MutableValue<Boolean> = MutableValue(false)
    override val selectedTab: MutableValue<Int> = MutableValue(0)

    val scope = componentCoroutineScope()

    override fun refreshAssignments() {
        scope.launch {
            isRefreshing.value = true
            val year = GeneralFlow.getYears(
                Data.selectedAccount.tenantUrl,
                Data.selectedAccount.tokens.accessToken,
                Data.selectedAccount.accountId
            ).first()

            assignments.value = AssignmentFlow.getAssignments(
                Url(Data.selectedAccount.tenantUrl),
                Data.selectedAccount.tokens.accessToken,
                Data.selectedAccount.accountId,
                0,
                250,
                year.start,
                year.end
            )

            isRefreshing.value = false
        }
    }

    init {
        refreshAssignments()
    }
}