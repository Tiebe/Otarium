package nl.tiebe.otarium.ui.home.settings.items.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.ui.home.settings.SettingsComponent
import nl.tiebe.otarium.ui.home.settings.utils.SettingRowIconButton
import nl.tiebe.otarium.utils.icons.Advertisements
import nl.tiebe.otarium.utils.icons.AdvertisementsOff
import nl.tiebe.otarium.utils.icons.BugOutline
import nl.tiebe.otarium.utils.icons.Icons
import androidx.compose.material.icons.Icons as MaterialIcons

@Composable
internal fun MainChildScreen(component: MainChildComponent) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        //users
        SettingRowIconButton(
            leftText = AnnotatedString(SettingsComponent.Config.Users.localizedString),
            icon = MaterialIcons.Default.AccountCircle
        ) {
            component.navigate(SettingsComponent.Config.Users)
        }

        //ads
        SettingRowIconButton(
            leftText = AnnotatedString(SettingsComponent.Config.Ads.localizedString),
            icon = if (Data.showAds) Icons.Advertisements else Icons.AdvertisementsOff
        ) {
            component.navigate(SettingsComponent.Config.Ads)
        }

        //bug report
        SettingRowIconButton(
            leftText = AnnotatedString(SettingsComponent.Config.Bugs.localizedString),
            icon = Icons.BugOutline
        ) {
            component.navigate(SettingsComponent.Config.Bugs)
        }

        //ui
        SettingRowIconButton(
            leftText = AnnotatedString(SettingsComponent.Config.UI.localizedString),
            icon = MaterialIcons.Default.Menu
        ) {
            component.navigate(SettingsComponent.Config.UI)
        }
    }
}