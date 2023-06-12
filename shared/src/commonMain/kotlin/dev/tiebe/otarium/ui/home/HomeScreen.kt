package dev.tiebe.otarium.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.tiebe.otarium.Data
import dev.tiebe.otarium.logic.root.home.HomeComponent
import dev.tiebe.otarium.logic.root.home.children.debug.DebugComponent
import dev.tiebe.otarium.logic.root.home.children.elo.ELOComponent
import dev.tiebe.otarium.logic.root.home.children.grades.GradesComponent
import dev.tiebe.otarium.logic.root.home.children.messages.MessagesComponent
import dev.tiebe.otarium.logic.root.home.children.settings.SettingsComponent
import dev.tiebe.otarium.logic.root.home.children.timetable.TimetableRootComponent
import dev.tiebe.otarium.ui.home.debug.DebugScreen
import dev.tiebe.otarium.ui.home.elo.ELOScreen
import dev.tiebe.otarium.ui.home.grades.GradesScreen
import dev.tiebe.otarium.ui.home.messages.MessagesScreen
import dev.tiebe.otarium.ui.home.settings.SettingsScreen
import dev.tiebe.otarium.ui.home.timetable.TimetableRootScreen
import dev.tiebe.otarium.utils.ui.getLocalizedString

var adsShown = mutableStateOf(Data.showAds)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(component: HomeComponent, windowInsets: WindowInsets) {
    val dialog = component.dialog.subscribeAsState()
    val overlay = dialog.value.child ?: return

    Scaffold(
        contentWindowInsets = windowInsets,
        bottomBar = {
            NavigationBar {
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