package nl.tiebe.otarium.ui.home.settings

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.ui.home.settings.items.ads.AdsChildScreen
import nl.tiebe.otarium.ui.home.settings.items.bugs.BugsChildScreen
import nl.tiebe.otarium.ui.home.settings.items.main.MainChildScreen
import nl.tiebe.otarium.ui.home.settings.items.users.UserChildScreen

@Composable
internal fun SettingsScreen(component: SettingsComponent) {
    val screen = component.childStack.subscribeAsState()

    when (val child = screen.value.active.instance) {
        is SettingsComponent.Child.MainChild -> MainChildScreen(child.component)
        is SettingsComponent.Child.AdsChild -> AdsChildScreen(child.component)
        is SettingsComponent.Child.UsersChild -> UserChildScreen(child.component)
        is SettingsComponent.Child.BugsChild -> BugsChildScreen(child.component)
    }
}
