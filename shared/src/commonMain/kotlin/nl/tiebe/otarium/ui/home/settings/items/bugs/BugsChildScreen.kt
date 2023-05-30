package nl.tiebe.otarium.ui.home.settings.items.bugs

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
fun BugsChildScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
/*
        Text(
            text = getLocalizedString(MR.strings.bug_text_1),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Divider()
*/

        Text(
            text = getLocalizedString(MR.strings.bug_text_2),
            modifier = Modifier.padding(vertical = 16.dp))
    }
}