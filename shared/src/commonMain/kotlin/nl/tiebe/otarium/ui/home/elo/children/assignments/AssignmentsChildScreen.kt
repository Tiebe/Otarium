package nl.tiebe.otarium.ui.home.elo.children.assignments

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.logic.root.home.children.elo.children.assignments.AssignmentsChildComponent
import nl.tiebe.otarium.ui.home.elo.children.assignments.assignment.AssignmentScreen
import nl.tiebe.otarium.ui.home.elo.children.assignments.assignment.AssignmentScreenTopAppBar
import nl.tiebe.otarium.ui.home.elo.children.assignments.listscreen.AssignmentListScreen
import nl.tiebe.otarium.utils.ui.getLocalizedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentsTopBar(component: AssignmentsChildComponent) {
    when (val instance = component.childStack.subscribeAsState().value.active.instance) {
        is AssignmentsChildComponent.Child.AssignmentList -> TopAppBar(title = { Text(getLocalizedString(MR.strings.assignments)) }, windowInsets = WindowInsets(0))
        is AssignmentsChildComponent.Child.Assignment -> AssignmentScreenTopAppBar(instance.component, component)
    }
}


@OptIn(ExperimentalDecomposeApi::class)
@Composable
internal fun AssignmentsChildScreen(component: AssignmentsChildComponent) {
    Box(modifier = Modifier.padding(start = 5.dp, end = 5.dp)) {
        Children(
            component.childStack.subscribeAsState().value,
            animation = predictiveBackAnimation(
                backHandler = component.backHandler,
                animation = stackAnimation(fade() + scale()), // Your usual animation here
                onBack = component::back,
            )
        ) { child ->
            Surface(Modifier.fillMaxSize()) {
                when (val instance = child.instance) {
                    is AssignmentsChildComponent.Child.Assignment -> AssignmentScreen(instance.component)
                    is AssignmentsChildComponent.Child.AssignmentList -> AssignmentListScreen(instance.component)
                }
            }
        }
    }
}