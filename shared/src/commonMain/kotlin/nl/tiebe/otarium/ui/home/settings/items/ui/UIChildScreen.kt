package nl.tiebe.otarium.ui.home.settings.items.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.ui.home.settings.utils.SettingSlider
import nl.tiebe.otarium.utils.ui.getLocalizedString
import kotlin.math.roundToInt
import nl.tiebe.otarium.MR

@Composable
internal fun UIChildScreen(component: UIChildComponent) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp))
    {
        var sliderValue by remember { mutableStateOf(Data.decimals.toFloat()) }

        SettingSlider(
            text = AnnotatedString(getLocalizedString(MR.strings.decimals_slider)),
            value = sliderValue,
            onValueChange = { sliderValue = it },
            onValueChangeFinished = {
                Data.decimals = sliderValue.roundToInt()
            },
            valueRange = 0f..4f,
            steps = 2
        )
    }
}