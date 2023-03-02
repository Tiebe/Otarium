package nl.tiebe.otarium.ui.screen.settings.items.ads

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.ui.icons.Advertisements
import nl.tiebe.otarium.ui.icons.AdvertisementsOff
import nl.tiebe.otarium.ui.screen.settings.utils.SettingRowIconButton
import nl.tiebe.otarium.ui.screen.settings.utils.SettingsItem
import nl.tiebe.otarium.utils.ui.getLocalizedString

internal class AdsSettingItem(parent: SettingsItem?) : SettingsItem(parent) {
    @Composable
    override fun ItemContent() {
        SettingRowIconButton(
            leftText = AnnotatedString(getLocalizedString(MR.strings.advertisements)),
            icon = if (Data.showAds) Advertisements else AdvertisementsOff) {
            openAsPopup()
        }
    }

    @Composable
    override fun ItemPopupContent() {
        super.ItemPopupContent()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = getLocalizedString(MR.strings.bug_text_1),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}