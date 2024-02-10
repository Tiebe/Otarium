package nl.tiebe.otarium.ui.home.settings.items.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.logic.root.home.children.settings.SettingsComponent
import nl.tiebe.otarium.logic.root.home.children.settings.children.ui.UIChildComponent
import nl.tiebe.otarium.ui.home.settings.utils.SettingRow
import nl.tiebe.otarium.ui.home.settings.utils.SettingRowIconButton
import nl.tiebe.otarium.ui.home.settings.utils.SettingSlider
import nl.tiebe.otarium.ui.home.settings.utils.SettingsRowToggle
import nl.tiebe.otarium.utils.OtariumIcons
import nl.tiebe.otarium.utils.otariumicons.Palette
import nl.tiebe.otarium.utils.ui.getLocalizedString
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UIChildScreen(component: UIChildComponent) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

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

        var decimals by remember { mutableStateOf(Data.decimals.toFloat()) }

        SettingSlider(
            text = AnnotatedString(getLocalizedString(MR.strings.decimals_slider)),
            value = decimals,
            onValueChange = { decimals = it },
            onValueChangeFinished = {
                Data.decimals = decimals.roundToInt()
            },
            valueRange = 0f..4f,
            steps = 3
        )

        var rounding by remember { mutableStateOf(Data.timetableRounding.toFloat()) }

        SettingSlider(
            text = AnnotatedString(getLocalizedString(MR.strings.timetable_rounding)),
            value = rounding,
            onValueChange = { rounding = it },
            onValueChangeFinished = {
                Data.timetableRounding = rounding.roundToInt()
            },
            valueRange = 0f..100f,
            steps = 99
        )

        val checkedStateTimetableContrast = component.timetableContrast.subscribeAsState()

        SettingsRowToggle(
            leftText = AnnotatedString(getLocalizedString(MR.strings.timetable_contrast)),
            checked = checkedStateTimetableContrast.value,
            rowClickable = true,
            onClick = {
                component.timetableContrast(it)
            }
        )

        val checkedStateMarkGrades = component.markGrades.subscribeAsState()

        SettingsRowToggle(
            leftText = AnnotatedString(getLocalizedString(MR.strings.mark_grades_red)),
            checked = checkedStateMarkGrades.value,
            rowClickable = true,
            onClick = {
                component.markGrades(it)
            }
        )

        val value = component.passingGrade.subscribeAsState().value

        //voldoendegrens input field
        SettingRow(
            text = AnnotatedString(getLocalizedString(MR.strings.passing_grade)),
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