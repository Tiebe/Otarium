package nl.tiebe.otarium.ui.home.settings.items.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.ui.home.settings.SettingsComponent
import nl.tiebe.otarium.ui.home.settings.utils.SettingRowIconButton
import nl.tiebe.otarium.ui.home.settings.utils.SettingSlider
import nl.tiebe.otarium.ui.home.settings.utils.SettingsRowToggle
import nl.tiebe.otarium.utils.OtariumIcons
import nl.tiebe.otarium.utils.otariumicons.Palette
import nl.tiebe.otarium.utils.ui.getLocalizedString
import kotlin.math.roundToInt

@Composable
internal fun UIChildScreen(component: UIChildComponent) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
         var sliderValue by remember { mutableStateOf(Data.decimals.toFloat()) }

        SettingRowIconButton(
            leftText = AnnotatedString(getLocalizedString(MR.strings.color_settings)),
            icon = OtariumIcons.Palette,
            rowClickable = true,
            onClick = {
                component.navigate(SettingsComponent.Config.Colors)
            }
        )

        val checkedStateCancelledLessons = component.showCancelledLessons.subscribeAsState()

        //todo: change url to remove status parameter
        SettingsRowToggle(
            leftText = AnnotatedString(getLocalizedString(MR.strings.show_cancelled_lessons)),
            checked = checkedStateCancelledLessons.value,
            rowClickable = true,
            onClick = {
                component.showCancelledLessons(it)
            }
        )

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