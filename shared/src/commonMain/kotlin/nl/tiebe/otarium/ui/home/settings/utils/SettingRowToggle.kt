package nl.tiebe.otarium.ui.home.settings.utils

import androidx.compose.foundation.clickable
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle

@Composable
fun SettingsRowToggle(modifier: Modifier = Modifier, leftText: AnnotatedString, textStyle: TextStyle = TextStyle(), checked: Boolean, rowClickable: Boolean, onClick: ((Boolean) -> Unit)?) {
    SettingRow(modifier = modifier.clickable(enabled = rowClickable, onClick = {
        if (onClick != null) {
            onClick(!checked)
        }
    }), text = leftText, textStyle = textStyle) {
        Switch(checked = checked, onCheckedChange = onClick)
    }
}
