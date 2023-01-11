package nl.tiebe.otarium.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.replaceCurrent
import nl.tiebe.otarium.ui.screen.agenda.AgendaScreen
import nl.tiebe.otarium.ui.screen.grades.GradeScreen
import nl.tiebe.otarium.ui.screen.settings.SettingsScreen
import nl.tiebe.otarium.utils.ui.getLocalizedString

@OptIn(ExperimentalMaterial3Api::class, ExperimentalDecomposeApi::class)
@Composable
internal fun BottomBar(
    navigator: StackNavigation<Screen>,
    modifier: Modifier
) {
    val currentScreen = remember { mutableStateOf<Screen>(Screen.Agenda) }

    val items = listOf(
        Screen.Agenda,
        Screen.Grades,
        Screen.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar(modifier = modifier, contentColor = MaterialTheme.colorScheme.onPrimary, containerColor = MaterialTheme.colorScheme.primary) {
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = screen.icon,
                        label = { Text(getLocalizedString(screen.resourceId)) },
                        selected = currentScreen.value == screen,
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
                source = navigator,
                initialStack = { listOf(currentScreen.value) },
                handleBackButton = true,
                animation = stackAnimation(fade() + com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale()),
            ) { screen ->
                currentScreen.value = screen

                when (screen) {
                    is Screen.Agenda -> AgendaScreen()
                    is Screen.Grades -> GradeScreen()
                    is Screen.Settings -> SettingsScreen()
                }
            }
        }
    }
}