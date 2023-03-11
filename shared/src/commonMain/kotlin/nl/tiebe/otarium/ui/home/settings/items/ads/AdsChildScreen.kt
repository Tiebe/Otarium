package nl.tiebe.otarium.ui.home.settings.items.ads

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.utils.ui.getLocalizedString

@Composable
internal fun AdsChildScreen(component: AdsChildComponent) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = getLocalizedString(MR.strings.advertisements),
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}