package nl.tiebe.otarium.ui.screen.settings.utils

import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle

@Composable
fun SettingsRowToggle(modifier: Modifier = Modifier, leftText: AnnotatedString, textStyle: TextStyle, checked: Boolean, onClick: ((Boolean) -> Unit)?) {
    SettingRow(modifier = modifier, text = leftText, textStyle = textStyle) {
        Switch(checked = checked, onCheckedChange = onClick)
    }
}
