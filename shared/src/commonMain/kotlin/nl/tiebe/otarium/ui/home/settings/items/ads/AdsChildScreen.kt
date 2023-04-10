package nl.tiebe.otarium.ui.home.settings.items.ads

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.ui.home.settings.utils.SettingsRowToggle
import nl.tiebe.otarium.utils.ui.getLocalizedString

@Composable
internal fun AdsChildScreen(component: AdsChildComponent) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val checkedStateAds = remember { mutableStateOf(Data.showAds) }
        val checkedStateAge = remember { mutableStateOf(Data.ageOfConsent) }

        SettingsRowToggle(
            leftText = AnnotatedString(getLocalizedString(MR.strings.show_ads_checkbox)),
            checked = checkedStateAds.value
        ) {
            checkedStateAds.value = it
            component.changeAdsState(it)
        }

        Divider()

        SettingsRowToggle(
            leftText = AnnotatedString(getLocalizedString(MR.strings.age_checkbox)),
            checked = checkedStateAge.value
        ) {
            checkedStateAge.value = it
            component.changeAgeOfConsent(it)
        }
    }
}