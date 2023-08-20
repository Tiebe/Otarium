package nl.tiebe.otarium.androidApp.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.icerock.moko.resources.compose.stringResource
import nl.tiebe.otarium.androidApp.ui.home.averages.AveragesScreen
import nl.tiebe.otarium.androidApp.ui.home.debug.DebugScreen
import nl.tiebe.otarium.androidApp.ui.home.elo.ELOScreen
import nl.tiebe.otarium.androidApp.ui.home.grades.GradesScreen
import nl.tiebe.otarium.androidApp.ui.home.messages.MessagesScreen
import nl.tiebe.otarium.androidApp.ui.home.settings.SettingsScreen
import nl.tiebe.otarium.androidApp.ui.home.timetable.TimetableRootScreen
import nl.tiebe.otarium.logic.root.home.HomeComponent
import nl.tiebe.otarium.logic.root.home.MenuItems
import nl.tiebe.otarium.logic.root.home.children.averages.AveragesComponent
import nl.tiebe.otarium.logic.root.home.children.debug.DebugComponent
import nl.tiebe.otarium.logic.root.home.children.elo.ELOComponent
import nl.tiebe.otarium.logic.root.home.children.grades.GradesComponent
import nl.tiebe.otarium.logic.root.home.children.messages.MessagesComponent
import nl.tiebe.otarium.logic.root.home.children.settings.SettingsComponent
import nl.tiebe.otarium.logic.root.home.children.timetable.TimetableRootComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(component: HomeComponent) {
    val dialog = component.dialog.subscribeAsState()
    val overlay = dialog.value.child ?: return

    Scaffold(
        bottomBar = {
            Navigation(component.visibleItems, overlay) {
                component.navigate(it)
            }
        }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            when (val dialogComponent = overlay.instance) {
                is TimetableRootComponent -> TimetableRootScreen(dialogComponent)
                is GradesComponent -> GradesScreen(dialogComponent)
                is AveragesComponent -> AveragesScreen(dialogComponent)
                is MessagesComponent -> MessagesScreen(dialogComponent)
                is ELOComponent -> ELOScreen(dialogComponent)
                is SettingsComponent -> SettingsScreen(dialogComponent)
                is DebugComponent -> DebugScreen(dialogComponent)
            }
        }
    }
}

@Composable
fun Navigation(visibleItems: List<MenuItems>, overlay: Child.Created<MenuItems, HomeComponent.MenuItemComponent>?, navigate: (MenuItems) -> Unit) {
    NavigationBar {
        visibleItems.forEach { screen ->
            NavigationBarItem(
                icon = if (overlay?.configuration == screen) screen.iconSelected else screen.icon,
                label = {
                    Text(
                        stringResource(screen.resourceId),
                        modifier = Modifier.wrapContentWidth(unbounded = true)
                    )
                },
                selected = overlay?.configuration == screen,
                onClick = {
                    navigate(screen)
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun HomeScreenPreview() {
    val visibleItems: List<MenuItems> = listOf(
        MenuItems.Timetable,
        MenuItems.Grades,
        MenuItems.Averages,
        MenuItems.Messages,
    )

    Scaffold(
        bottomBar = {
            Navigation(visibleItems, null) {}
        }
    ) {}
}