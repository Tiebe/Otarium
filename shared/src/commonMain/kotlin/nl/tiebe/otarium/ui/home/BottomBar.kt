package nl.tiebe.otarium.ui.home

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
import nl.tiebe.otarium.oldui.screen.agenda.AgendaScreen
import nl.tiebe.otarium.oldui.screen.grades.GradeScreen
import nl.tiebe.otarium.oldui.screen.settings.SettingsScreen
import nl.tiebe.otarium.ui.home.agenda.AgendaComponent
import nl.tiebe.otarium.ui.home.grades.GradesComponent
import nl.tiebe.otarium.ui.home.settings.SettingsComponent
import nl.tiebe.otarium.utils.ui.getLocalizedString

@OptIn(ExperimentalMaterial3Api::class, ExperimentalDecomposeApi::class)
@Composable
internal fun BottomBar(
    component: HomeComponent,
    modifier: Modifier,
    onNewUser: () -> Unit
) {

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
            val dialog = component.dialog.subscribeAsState()
            val overlay = dialog.value.overlay ?: return@Box

            when (val component = overlay.instance) {
                    is AgendaComponent -> AgendaScreen(component)
                    is GradesComponent -> GradeScreen(component)
                    is SettingsComponent -> SettingsScreen(component)
            }
            }
        }
    }
}