package nl.tiebe.otarium.android.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import nl.tiebe.otarium.android.R
import nl.tiebe.otarium.android.ui.LabelledCheckBox
import nl.tiebe.otarium.showAds
import nl.tiebe.otarium.android.ui.adsShown


@Preview(showBackground = true)
@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(id = R.string.settings_title),
        textAlign = TextAlign.Center)
        val checkedState = remember { mutableStateOf(showAds()) }
        LabelledCheckBox(checked = checkedState.value, onCheckedChange = {
            checkedState.value = it
            showAds(checkedState.value)
            adsShown = it
        }, label = stringResource(id = R.string.show_ads_checkbox))

    }
}
