package nl.tiebe.otarium.ui.screen.settings.items.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.ui.screen.settings.items.ads.AdsSettingItem
import nl.tiebe.otarium.ui.screen.settings.utils.SettingsItem
import nl.tiebe.otarium.utils.ui.getLocalizedString

internal class MainSettingItem : SettingsItem(null) {
    @Composable
    override fun ItemContent() {}

    @Composable
    override fun ItemPopupContent() {
        super.ItemPopupContent()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val adsSettingsItem by remember { mutableStateOf(AdsSettingItem(this@MainSettingItem)) }
            adsSettingsItem.ItemContent()

            Text(
                text = getLocalizedString(MR.strings.bug_text_1),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}