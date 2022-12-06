package nl.tiebe.otarium.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import nl.tiebe.otarium.ui.screen.SettingsScreen
import nl.tiebe.otarium.ui.screen.agenda.AgendaScreen
import nl.tiebe.otarium.ui.screen.grades.GradeScreen
import nl.tiebe.otarium.utils.getLocalizedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(screenState: MutableState<Screen>, modifier: Modifier) {
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
                        selected = screenState.value == screen,
                        onClick = {
                            screenState.value = screen
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            when (screenState.value) {
                is Screen.Agenda -> AgendaScreen()
                is Screen.Grades -> GradeScreen()
                is Screen.Settings -> SettingsScreen()
            }
        }
    }
}