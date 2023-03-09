package nl.tiebe.otarium.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.ui.home.timetable.TimetableComponent
import nl.tiebe.otarium.ui.home.timetable.TimetableScreen
import nl.tiebe.otarium.ui.home.grades.GradesComponent
import nl.tiebe.otarium.ui.home.grades.GradesScreen
import nl.tiebe.otarium.ui.home.settings.SettingsComponent
import nl.tiebe.otarium.ui.home.settings.SettingsScreen
import nl.tiebe.otarium.utils.ui.getLocalizedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BottomBar(
    component: HomeComponent,
    modifier: Modifier,
) {
    println("sSAdsadsa")
    val dialog = component.dialog.subscribeAsState()
    val overlay = dialog.value.overlay ?: return

    val items = listOf(
        HomeComponent.MenuItem.Timetable,
        HomeComponent.MenuItem.Grades,
        HomeComponent.MenuItem.Settings
    )
    println("Overlay: ${overlay.configuration}")
    println("Fasfdsadfsdafas")

    Scaffold(
        bottomBar = {
            NavigationBar(modifier = modifier, contentColor = MaterialTheme.colorScheme.onPrimary, containerColor = MaterialTheme.colorScheme.primary) {
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = screen.icon,
                        label = { Text(getLocalizedString(screen.resourceId)) },
                        selected = overlay.configuration == screen,
                        onClick = {
                            component.navigate(screen)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            when (val dialogComponent = overlay.instance) {
                is TimetableComponent -> TimetableScreen(dialogComponent)
                is GradesComponent -> GradesScreen(dialogComponent)
                is SettingsComponent -> SettingsScreen(dialogComponent)
            }
        }
    }
}