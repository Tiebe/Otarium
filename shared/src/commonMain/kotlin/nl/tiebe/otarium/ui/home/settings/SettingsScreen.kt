package nl.tiebe.otarium.ui.home.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.ui.home.settings.items.ads.AdsChildScreen
import nl.tiebe.otarium.ui.home.settings.items.bugs.BugsChildScreen
import nl.tiebe.otarium.ui.home.settings.items.main.MainChildScreen
import nl.tiebe.otarium.ui.home.settings.items.ui.UIChildScreen
import nl.tiebe.otarium.ui.home.settings.items.users.UserChildScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsScreen(component: SettingsComponent) {
    Column {
        val screen = component.childStack.subscribeAsState()

        if (screen.value.active.instance !is SettingsComponent.Child.MainChild) {
            TopAppBar(
                title = { Text(screen.value.active.configuration.localizedString) },
                navigationIcon = {
                    IconButton(onClick = { component.back() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }

        Box(modifier = Modifier.padding(start = 5.dp, end = 5.dp)) {
            when (val child = screen.value.active.instance) {
                is SettingsComponent.Child.MainChild -> MainChildScreen(child.component)
                is SettingsComponent.Child.AdsChild -> AdsChildScreen(child.component)
                is SettingsComponent.Child.UsersChild -> UserChildScreen(child.component)
                is SettingsComponent.Child.BugsChild -> BugsChildScreen()
                is SettingsComponent.Child.UIChild -> UIChildScreen(child.component)
            }
        }
    }
}
