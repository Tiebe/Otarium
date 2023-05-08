package nl.tiebe.otarium.ui.home.elo.children.assignments.assignment

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.api.assignment.AssignmentFlow
import dev.tiebe.magisterapi.response.assignment.Assignment
import dev.tiebe.magisterapi.response.assignment.AssignmentVersion
import io.ktor.http.*
import kotlinx.coroutines.launch
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.ui.root.componentCoroutineScope

interface AssignmentScreenComponent {
    val assignment: Value<Assignment>
    val versions: Value<List<AssignmentVersion>>

    val assignmentLink: String

    val isRefreshing: Value<Boolean>

    fun refreshAssignment()

    suspend fun getVersions(assignment: Assignment)

}

class DefaultAssignmentScreenComponent(componentContext: ComponentContext, override val assignmentLink: String): AssignmentScreenComponent, ComponentContext by componentContext {
    override val assignment: MutableValue<Assignment> = MutableValue(Assignment(false, null, "", listOf(), null, 0, null, "", 0, listOf(), false, "", false, 0, "", "", listOf(), false))
    override val versions: MutableValue<List<AssignmentVersion>> = MutableValue(listOf())

    override val isRefreshing: MutableValue<Boolean> = MutableValue(false)
    val scope = componentCoroutineScope()

    override fun refreshAssignment() {
        scope.launch {
            isRefreshing.value = true
            val tempAssignment = AssignmentFlow.getFullAssignment(Url(Data.selectedAccount.tenantUrl), Data.selectedAccount.tokens.accessToken, assignmentLink)
            getVersions(tempAssignment)
            assignment.value = tempAssignment
            isRefreshing.value = false
        }
    }

    override suspend fun getVersions(assignment: Assignment) {
        val list = mutableListOf<AssignmentVersion>()

        assignment.navigationItemsVersion.forEach {
            println("test")
            list.add(
                AssignmentFlow.getVersionInfo(
                    Url(Data.selectedAccount.tenantUrl),
                    Data.selectedAccount.tokens.accessToken,
                    it.links.first { link -> link.rel == "Self" }.href
                )
            )
        }

        versions.value = list
    }


    init {
        refreshAssignment()
    }


}