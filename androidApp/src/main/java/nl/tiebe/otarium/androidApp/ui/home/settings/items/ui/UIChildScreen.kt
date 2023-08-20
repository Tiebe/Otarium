package nl.tiebe.otarium.androidApp.ui.home.settings.items.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.androidApp.ui.home.settings.utils.SettingRow
import nl.tiebe.otarium.androidApp.ui.home.settings.utils.SettingRowIconButton
import nl.tiebe.otarium.androidApp.ui.home.settings.utils.SettingSlider
import nl.tiebe.otarium.androidApp.ui.home.settings.utils.SettingsRowToggle
import nl.tiebe.otarium.logic.root.home.children.settings.SettingsComponent
import nl.tiebe.otarium.logic.root.home.children.settings.children.ui.UIChildComponent
import nl.tiebe.otarium.utils.OtariumIcons
import nl.tiebe.otarium.utils.otariumicons.Palette
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UIChildScreen(component: UIChildComponent) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
         var sliderValue by remember { mutableStateOf(Data.decimals.toFloat()) }

        SettingRowIconButton(
            leftText = AnnotatedString(stringResource(MR.strings.color_settings.resourceId)),
            icon = OtariumIcons.Palette,
            rowClickable = true,
            onClick = {
                component.navigate(SettingsComponent.Config.Colors)
            }
        )

        val checkedStateCancelledLessons = component.showCancelledLessons.subscribeAsState()

        //todo: change url to remove status parameter
        SettingsRowToggle(
            leftText = AnnotatedString(stringResource(MR.strings.show_cancelled_lessons.resourceId)),
            checked = checkedStateCancelledLessons.value,
            rowClickable = true,
            onClick = {
                component.showCancelledLessons(it)
            }
        )

        SettingSlider(
            text = AnnotatedString(stringResource(MR.strings.decimals_slider.resourceId)),
            value = sliderValue,
            onValueChange = { sliderValue = it },
            onValueChangeFinished = {
                Data.decimals = sliderValue.roundToInt()
            },
            valueRange = 0f..4f,
            steps = 2
        )

        val checkedStateMarkGrades = component.markGrades.subscribeAsState()

        SettingsRowToggle(
            leftText = AnnotatedString(stringResource(MR.strings.mark_grades_red.resourceId)),
            checked = checkedStateMarkGrades.value,
            rowClickable = true,
            onClick = {
                component.markGrades(it)
            }
        )

        val value = component.passingGrade.subscribeAsState().value

        //voldoendegrens input field
        SettingRow(
            text = AnnotatedString(stringResource(MR.strings.passing_grade.resourceId)),
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = { newValue ->
                    if ((newValue.replace(",", ".").replace(".", "").all { it.isDigit() } &&
                                newValue.replace(",", ".").toFloatOrNull() != null) || newValue.isBlank())
                        component.passingGrade(newValue)
                },
                isError = !((value.replace(",", ".").replace(".", "").all { it.isDigit() } &&
                        value.replace(",", ".").toFloatOrNull() != null) || value.isBlank()),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(100.dp)
            )
        }

    }
}