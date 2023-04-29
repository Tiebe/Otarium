package nl.tiebe.otarium.ui.home.elo.children.assignments

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.ui.home.elo.children.assignments.listscreen.AssignmentListScreen

@Composable
internal fun AssignmentsChildScreen(component: AssignmentsChildComponent) {
    val child = component.childStack.subscribeAsState().value

    Box(modifier = Modifier.padding(start = 5.dp, end = 5.dp)) {
        when (val screen = child.active.instance) {
            is AssignmentsChildComponent.Child.Assignment -> TODO()
            is AssignmentsChildComponent.Child.AssignmentList -> AssignmentListScreen(screen.component)
        }
    }

}