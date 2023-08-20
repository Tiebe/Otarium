package nl.tiebe.otarium.androidApp.ui.home.elo.children.assignments

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.router.stack.pop
import kotlinx.coroutines.launch
import nl.tiebe.otarium.androidApp.ui.home.elo.children.assignments.assignment.AssignmentScreen
import nl.tiebe.otarium.androidApp.ui.home.elo.children.assignments.listscreen.AssignmentListScreen
import nl.tiebe.otarium.logic.root.home.children.elo.children.assignments.AssignmentsChildComponent

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun AssignmentsChildScreen(component: AssignmentsChildComponent) {
    val screen = component.childStack.subscribeAsState()
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.padding(start = 5.dp, end = 5.dp)) {
        when (val child = screen.value.items[0].instance) {
            is AssignmentsChildComponent.Child.Assignment -> AssignmentScreen(child.component)
            is AssignmentsChildComponent.Child.AssignmentList -> AssignmentListScreen(child.component)
        }


        for (item in screen.value.items.subList(1, screen.value.items.size)) {
            val state = rememberDismissState()

            component.onBack.value = {
                scope.launch {
                    state.animateTo(DismissValue.DismissedToEnd)
                }
            }

            //pop on finish
            if (state.isDismissed(DismissDirection.StartToEnd)) {
                component.navigation.pop()
            }

            SwipeToDismiss(
                state = state,
                background = {
                },
                directions = setOf(DismissDirection.StartToEnd)
            ) {
                Surface(Modifier.fillMaxSize()) {
                    when (val child = item.instance) {
                        is AssignmentsChildComponent.Child.Assignment -> AssignmentScreen(child.component)
                        is AssignmentsChildComponent.Child.AssignmentList -> AssignmentListScreen(child.component)
                    }
                }
            }
        }
    }



    Box(modifier = Modifier.padding(start = 5.dp, end = 5.dp)) {

    }

}