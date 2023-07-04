package nl.tiebe.otarium.ui.home.averages

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.logic.root.home.children.averages.AveragesComponent
import nl.tiebe.otarium.ui.home.averages.subject.GCSubjectList

@Composable
internal fun GradeCalculationChild(component: AveragesComponent) {
    when (val state = component.state.subscribeAsState().value) {
        AveragesComponent.State.Loading -> {
            Text("Loading")
        }
        is AveragesComponent.State.Data -> {
            GCSubjectList(component, state.data.sortedBy { it.grade.dateEntered }, component.manualGradesList.subscribeAsState().value)
        }
        is AveragesComponent.State.Failed -> {
            Text("Something went wrong while retrieving your grades")
        }
    }
}
