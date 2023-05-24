package nl.tiebe.otarium.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.ui.home.debug.DebugComponent
import nl.tiebe.otarium.ui.home.debug.DebugScreen
import nl.tiebe.otarium.ui.home.elo.ELOComponent
import nl.tiebe.otarium.ui.home.elo.ELOScreen
import nl.tiebe.otarium.ui.home.grades.GradesComponent
import nl.tiebe.otarium.ui.home.grades.GradesScreen
import nl.tiebe.otarium.ui.home.messages.MessagesComponent
import nl.tiebe.otarium.ui.home.messages.MessagesScreen
import nl.tiebe.otarium.ui.home.settings.SettingsComponent
import nl.tiebe.otarium.ui.home.settings.SettingsScreen
import nl.tiebe.otarium.ui.home.timetable.TimetableRootComponent
import nl.tiebe.otarium.ui.home.timetable.TimetableRootScreen
import nl.tiebe.otarium.utils.ui.getLocalizedString

var adsShown = mutableStateOf(Data.showAds)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(component: HomeComponent, windowInsets: WindowInsets) {
    val dialog = component.dialog.subscribeAsState()
    val overlay = dialog.value.child ?: return

    Scaffold(
        contentWindowInsets = windowInsets,
        bottomBar = {
            Column {
                NavigationBar(windowInsets = if (adsShown.value) WindowInsets(0, 0, 0, 0) else NavigationBarDefaults.windowInsets) {
                    component.visibleItems.forEach { screen ->
                        NavigationBarItem(
                            icon = if (overlay.configuration == screen) screen.iconSelected else screen.icon,
                            label = {
                                Text(
                                    getLocalizedString(screen.resourceId),
                                    modifier = Modifier.wrapContentWidth(unbounded = true)
                                )
                            },
                            selected = overlay.configuration == screen,
                            onClick = {
                                component.navigate(screen)
                            }
                        )
                    }
                }

                if (adsShown.value) {
                    Surface(modifier = Modifier.height(50.dp), color = NavigationBarDefaults.containerColor, tonalElevation = NavigationBarDefaults.Elevation) {
                        Ads()
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            when (val dialogComponent = overlay.instance) {
                is TimetableRootComponent -> TimetableRootScreen(dialogComponent)
                is GradesComponent -> GradesScreen(dialogComponent)
                is MessagesComponent -> MessagesScreen(dialogComponent)
                is ELOComponent -> ELOScreen(dialogComponent)
                is SettingsComponent -> SettingsScreen(dialogComponent)
                is DebugComponent -> DebugScreen(dialogComponent)
            }
        }
    }
}

@Composable
internal expect fun Ads()