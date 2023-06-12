package dev.tiebe.otarium.ui.home.settings.items.ads

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import dev.tiebe.otarium.Data
import dev.tiebe.otarium.MR
import dev.tiebe.otarium.ui.home.settings.utils.SettingsRowToggle
import dev.tiebe.otarium.utils.ui.getLocalizedString

@Composable
internal fun AdsChildScreen(component: AdsChildComponent) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val checkedStateAds = remember { mutableStateOf(Data.showAds) }
        val checkedStateAge = remember { mutableStateOf(Data.ageOfConsent) }

        Divider()
        SettingsRowToggle(
            leftText = AnnotatedString(getLocalizedString(MR.strings.show_ads_checkbox)),
            checked = checkedStateAds.value,
            rowClickable = true,
        ) {
            checkedStateAds.value = it
            component.changeAdsState(it)
        }

        SettingsRowToggle(
            leftText = AnnotatedString(getLocalizedString(MR.strings.age_checkbox)),
            checked = checkedStateAge.value,
            rowClickable = true,
        ) {
            checkedStateAge.value = it
            component.changeAgeOfConsent(it)
        }
    }
}