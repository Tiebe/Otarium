package nl.tiebe.otarium.androidApp.ui.home.settings.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
internal fun SettingsColorPicker(modifier: Modifier = Modifier, leftText: AnnotatedString, textStyle: TextStyle = TextStyle()) {
    val showPicker = remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(0.95f).height(70.dp).clickable { showPicker.value = !showPicker.value }.then(modifier),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = leftText, style = textStyle.merge(LocalTextStyle.current))

            Button(
                modifier = Modifier.width(50.dp),
                onClick = { showPicker.value = !showPicker.value },
                contentPadding = PaddingValues(0.dp),
                //colors = ButtonDefaults.buttonColors(containerColor = color.toColor())
            ) {}
        }

        Divider()
    }
}
