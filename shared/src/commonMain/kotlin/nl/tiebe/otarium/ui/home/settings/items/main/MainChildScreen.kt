package nl.tiebe.otarium.ui.home.settings.items.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.oldui.screen.settings.utils.SettingRowIconButton
import nl.tiebe.otarium.ui.home.settings.SettingsComponent
import nl.tiebe.otarium.ui.icons.Advertisements
import nl.tiebe.otarium.ui.icons.AdvertisementsOff
import nl.tiebe.otarium.utils.ui.getLocalizedString

@Composable
internal fun MainChildScreen(component: MainChildComponent) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        //ads
        SettingRowIconButton(
            leftText = AnnotatedString(getLocalizedString(MR.strings.advertisements)),
            icon = if (Data.showAds) Advertisements else AdvertisementsOff
        ) {
            component.navigate(SettingsComponent.Config.Ads)
        }

        Text(
            text = getLocalizedString(MR.strings.bug_text_1),
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}