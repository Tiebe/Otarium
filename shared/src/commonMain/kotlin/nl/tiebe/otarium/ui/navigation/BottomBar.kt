package nl.tiebe.otarium.ui.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.navigation.BackStackEntry
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo
import nl.tiebe.otarium.utils.getLocalizedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BottomBar(navigator: Navigator, modifier: Modifier, currentItemRoute: State<BackStackEntry?>) {
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
                        selected = currentItemRoute.value?.route?.route == screen.route,
                        onClick = {
                            navigator.navigate(screen.route, NavOptions(
                                true,
                                PopUpTo.First()
                            ))
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHostController(navigator, innerPadding)
    }
}