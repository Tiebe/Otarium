package nl.tiebe.otarium.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import nl.tiebe.otarium.ui.screen.agenda.AgendaScreen
import nl.tiebe.otarium.ui.screen.grades.GradeScreen
import nl.tiebe.otarium.ui.screen.settings.SettingsScreen
import nl.tiebe.otarium.utils.ui.getLocalizedString

@OptIn(ExperimentalMaterial3Api::class, ExperimentalDecomposeApi::class)
@Composable
internal fun BottomBar(
    childStack: Value<ChildStack<Screen, ComponentContext>>,
    componentContext: ComponentContext,
    navigator: StackNavigation<Screen>,
    modifier: Modifier,
    onNewUser: () -> Unit
) {
    val items = listOf(
        Screen.Agenda,
        Screen.Grades,
        Screen.Settings,
    )

    val state = childStack.subscribeAsState()

    Scaffold(
        bottomBar = {
            NavigationBar(modifier = modifier, contentColor = MaterialTheme.colorScheme.onPrimary, containerColor = MaterialTheme.colorScheme.primary) {
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = screen.icon,
                        label = { Text(getLocalizedString(screen.resourceId)) },
                        selected = state.value.active.configuration == screen,
                        onClick = {
                            navigator.replaceCurrent(screen)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            ChildStack(
                childStack = childStack,
                animation = stackAnimation(fade() + scale()),
            ) { screen ->
                when (screen) {
                    is Screen.Agenda -> AgendaScreen(componentContext)
                    is Screen.Grades -> GradeScreen(componentContext)
                    is Screen.Settings -> SettingsScreen(componentContext, onNewUser)
                }
            }
        }
    }
}