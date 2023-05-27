package nl.tiebe.otarium.ui.home.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.router.stack.pop
import kotlinx.coroutines.launch
import nl.tiebe.otarium.ui.home.settings.items.ads.AdsChildScreen
import nl.tiebe.otarium.ui.home.settings.items.bugs.BugsChildScreen
import nl.tiebe.otarium.ui.home.settings.items.main.MainChildScreen
import nl.tiebe.otarium.ui.home.settings.items.ui.UIChildScreen
import nl.tiebe.otarium.ui.home.settings.items.ui.colors.ColorChildScreen
import nl.tiebe.otarium.ui.home.settings.items.users.UserChildScreen

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun SettingsScreen(component: SettingsComponent) {
    val screen = component.childStack.subscribeAsState()
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.padding(start = 5.dp, end = 5.dp)) {
        for (item in screen.value.items) {
            println(item.configuration)
        }

        SettingsScreenChild(component, screen.value.items[0])

        for (item in screen.value.items.subList(1, screen.value.items.size)) {
            val state = rememberDismissState()

            component.onBack.value = {
                scope.launch {
                    state.animateTo(DismissValue.DismissedToEnd)
                }
            }

            //pop on finish
            if (state.isDismissed(DismissDirection.StartToEnd)) {
                component.navigation.pop()
            }

            SwipeToDismiss(
                state = state,
                background = {
                },
                directions = setOf(DismissDirection.StartToEnd)
            ) {
                Surface(Modifier.fillMaxSize()) {
                    SettingsScreenChild(component, item)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsScreenChild(
    component: SettingsComponent,
    screen: Child.Created<SettingsComponent.Config, SettingsComponent.Child>
) {
    Column {
        TopAppBar(
            title = { Text(screen.configuration.localizedString) },
            navigationIcon = {
                if (screen.instance !is SettingsComponent.Child.MainChild) {
                    IconButton(onClick = { component.back() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            },
            windowInsets = WindowInsets(0.dp)
        )

        Box(modifier = Modifier.padding(start = 5.dp, end = 5.dp)) {
            when (val child = screen.instance) {
                is SettingsComponent.Child.MainChild -> MainChildScreen(child.component)
                is SettingsComponent.Child.AdsChild -> AdsChildScreen(child.component)
                is SettingsComponent.Child.UsersChild -> UserChildScreen(child.component)
                is SettingsComponent.Child.BugsChild -> BugsChildScreen()
                is SettingsComponent.Child.UIChild -> UIChildScreen(child.component)
                is SettingsComponent.Child.ColorChild -> ColorChildScreen(child.component)
            }
        }
    }
}