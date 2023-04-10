package nl.tiebe.otarium.ui.home.settings.utils

import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle

@Composable
internal fun SettingsRowToggle(modifier: Modifier = Modifier, leftText: AnnotatedString, textStyle: TextStyle = TextStyle(), checked: Boolean, onClick: ((Boolean) -> Unit)?) {
    SettingRow(modifier = modifier, text = leftText, textStyle = textStyle) {
        Switch(checked = checked, onCheckedChange = onClick)
    }
}
