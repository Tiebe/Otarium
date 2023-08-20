package nl.tiebe.otarium.androidApp.ui.home.settings.items.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import nl.tiebe.otarium.androidApp.ui.home.settings.utils.SettingRowIconButton
import nl.tiebe.otarium.logic.root.home.children.settings.SettingsComponent
import nl.tiebe.otarium.logic.root.home.children.settings.children.main.MainChildComponent
import androidx.compose.material.icons.Icons as MaterialIcons

@Composable
internal fun MainChildScreen(component: MainChildComponent) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        //ui
        SettingRowIconButton(
            leftText = AnnotatedString(SettingsComponent.Config.UI.toString() /*todo*/),
            icon = MaterialIcons.Default.Menu,
            rowClickable = true,
        ) {
            component.navigate(SettingsComponent.Config.UI)
        }
    }
}