package nl.tiebe.otarium.oldui.screen.settings.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun SettingRowIconButton(modifier: Modifier = Modifier, leftText: AnnotatedString, textStyle: TextStyle = TextStyle(), icon: ImageVector, description: String = leftText.text, onClick: () -> Unit) {
    SettingRow(modifier = modifier, text = leftText, textStyle = textStyle) {
        Button(modifier = Modifier.width(50.dp), onClick = onClick, contentPadding = PaddingValues(0.dp)) {
            Icon(imageVector = icon, contentDescription = description, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun SettingRowTextButton(modifier: Modifier = Modifier, leftText: AnnotatedString, textStyle: TextStyle = TextStyle(), rightText: AnnotatedString, onClick: () -> Unit) {
    SettingRow(modifier = modifier, text = leftText, textStyle = textStyle) {
        Button(onClick = onClick, contentPadding = PaddingValues(0.dp)) {
            Text(text = rightText, modifier = Modifier.fillMaxWidth())
        }
    }
}

