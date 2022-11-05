package nl.tiebe.otarium.android.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.tiebe.otarium.android.R
import nl.tiebe.otarium.android.ui.adsShown
import nl.tiebe.otarium.showAds

//TODO: Wait for this pull request to be merged and then use this library: https://github.com/alorma/Compose-Settings/pull/65

@Preview(showBackground = true)
@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(modifier = Modifier.fillMaxWidth(0.95f), horizontalArrangement = Arrangement.SpaceBetween) {
            val checkedState = remember { mutableStateOf(showAds()) }

            Text(text = stringResource(id = R.string.show_ads_checkbox),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 12.dp))

            Switch(checked = checkedState.value, onCheckedChange = {
                checkedState.value = it
                showAds(it)
                adsShown = it
            })

        }
    }
}
