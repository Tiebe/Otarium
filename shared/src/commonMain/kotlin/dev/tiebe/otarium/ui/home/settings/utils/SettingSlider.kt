package dev.tiebe.otarium.ui.home.settings.utils

import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

@Composable
internal fun SettingSlider(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    text: AnnotatedString,
    textStyle: TextStyle = TextStyle(textAlign = TextAlign.Center),
    value: Float = 0f,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int = 0
) {
    Column(modifier = Modifier.fillMaxWidth(0.95f).then(modifier), horizontalAlignment = horizontalAlignment, verticalArrangement = verticalArrangement) {
        Text(text = text, style = LocalTextStyle.current.merge(textStyle))


        Slider(
            value = value,
            onValueChange = onValueChange,
            onValueChangeFinished = onValueChangeFinished,
            valueRange = valueRange,
            steps = steps,
        )

        Row {
            Text(text = AnnotatedString(valueRange.start.toInt().toString()))
            Spacer(modifier = Modifier.weight(1f))
            Text(text = AnnotatedString(valueRange.endInclusive.toInt().toString()))
        }
    }
}