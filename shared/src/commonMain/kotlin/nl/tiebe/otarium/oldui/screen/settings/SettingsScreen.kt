package nl.tiebe.otarium.oldui.screen.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import nl.tiebe.otarium.closeApp
import nl.tiebe.otarium.oldui.screen.settings.items.main.MainSettingItem


@Composable
internal fun SettingsScreen(componentContext: ComponentContext, onNewUser: () -> Unit) {
    val mainScreen by remember { mutableStateOf(MainSettingItem()) }

    mainScreen.ItemPopup()

    componentContext.backHandler.register(
        BackCallback {
            if (!mainScreen.closePopup()) {
                // if no popups to close, close app
                closeApp()
            }
        }
    )
}
