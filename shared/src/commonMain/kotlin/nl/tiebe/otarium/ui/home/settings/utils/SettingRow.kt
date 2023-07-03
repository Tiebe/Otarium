package nl.tiebe.otarium.ui.home.settings.utils

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
internal fun SettingRow(modifier: Modifier = Modifier,
               horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
               verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
               text: AnnotatedString,
               textStyle: TextStyle = TextStyle(textAlign = TextAlign.Center),
               content: @Composable RowScope.() -> Unit) {

    Row(modifier = Modifier.fillMaxWidth(0.95f).height(70.dp).then(modifier),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment) {
        Text(text = text, style = textStyle.merge(LocalTextStyle.current))
        content()
    }

    Divider()
}