package dev.tiebe.otarium.ui.home.settings.items.ui.colors

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.tiebe.otarium.MR
import dev.tiebe.otarium.logic.home.children.settings.children.ui.children.colors.ColorChildComponent
import dev.tiebe.otarium.ui.home.settings.utils.SettingRowIconButton
import dev.tiebe.otarium.ui.home.settings.utils.SettingsColorPicker
import dev.tiebe.otarium.ui.home.settings.utils.SettingsRowToggle
import dev.tiebe.otarium.utils.OtariumIcons
import dev.tiebe.otarium.utils.dynamicColorsPossible
import dev.tiebe.otarium.utils.otariumicons.ContentSave
import dev.tiebe.otarium.utils.ui.getLocalizedString

@Composable
internal fun ColorChildScreen(component: ColorChildComponent) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        val dynamicColorScheme = component.dynamicColorState.subscribeAsState()
        val customColorScheme = component.customColorScheme.subscribeAsState()

        if (dynamicColorsPossible()) {
            SettingsRowToggle(
                leftText = AnnotatedString(getLocalizedString(MR.strings.color_dynamic)),
                checked = dynamicColorScheme.value,
                rowClickable = true
            ) {
                component.dynamicColorState.value = it
            }
        }

        SettingsRowToggle(
            leftText = AnnotatedString(getLocalizedString(MR.strings.color_custom)),
            checked = customColorScheme.value,
            rowClickable = true
        ) {
            component.customColorScheme.value = it
        }

        if (customColorScheme.value) {
            SettingRowIconButton(
                leftText = AnnotatedString(getLocalizedString(MR.strings.color_reset)),
                icon = Icons.Default.Refresh,
                rowClickable = false,
                onClick = {
                    component.resetColorScheme()
                }
            )

            SettingRowIconButton(
                leftText = AnnotatedString(getLocalizedString(MR.strings.color_save)),
                icon = OtariumIcons.ContentSave,
                rowClickable = false,
                onClick = {
                    component.saveColorScheme()
                }
            )

            val showLightColors = remember { mutableStateOf(false) }

            Row(modifier = Modifier.fillMaxWidth(0.95f).height(70.dp).clickable { showLightColors.value = !showLightColors.value },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Light")

                Button(modifier = Modifier.width(50.dp), onClick = { showLightColors.value = !showLightColors.value }, contentPadding = PaddingValues(0.dp)) {
                    Icon(Icons.Default.Create, contentDescription = "Create")
                }
            }

            AnimatedVisibility(modifier = Modifier.padding(start = 16.dp), visible = showLightColors.value, enter = expandVertically(), exit = shrinkVertically()) {
                Column {
                    SettingsColorPicker(
                        leftText = AnnotatedString(getLocalizedString(MR.strings.color_primary)),
                        color = component.primaryLightColor.subscribeAsState().value
                    ) {
                        component.primaryLightColor.value = it
                    }

                    SettingsColorPicker(
                        leftText = AnnotatedString(getLocalizedString(MR.strings.color_secondary)),
                        color = component.secondaryLightColor.subscribeAsState().value
                    ) {
                        component.secondaryLightColor.value = it
                    }

                    SettingsColorPicker(
                        leftText = AnnotatedString(getLocalizedString(MR.strings.color_tertiary)),
                        color = component.tertiaryLightColor.subscribeAsState().value
                    ) {
                        component.tertiaryLightColor.value = it
                    }
                }
            }

            val showDarkColors = remember { mutableStateOf(false) }

            Row(modifier = Modifier.fillMaxWidth(0.95f).height(70.dp).clickable { showDarkColors.value = !showDarkColors.value },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Dark")

                Button(modifier = Modifier.width(50.dp), onClick = { showDarkColors.value = !showDarkColors.value }, contentPadding = PaddingValues(0.dp)) {
                    Icon(Icons.Default.Create, contentDescription = "Create")
                }
            }

            AnimatedVisibility(modifier = Modifier.padding(start = 16.dp), visible = showDarkColors.value, enter = expandVertically(), exit = shrinkVertically()) {
                Column {
                    SettingsColorPicker(
                        leftText = AnnotatedString(getLocalizedString(MR.strings.color_primary)),
                        color = component.primaryDarkColor.subscribeAsState().value
                    ) {
                        component.primaryDarkColor.value = it
                    }

                    SettingsColorPicker(
                        leftText = AnnotatedString(getLocalizedString(MR.strings.color_secondary)),
                        color = component.secondaryDarkColor.subscribeAsState().value
                    ) {
                        component.secondaryDarkColor.value = it
                    }

                    SettingsColorPicker(
                        leftText = AnnotatedString(getLocalizedString(MR.strings.color_tertiary)),
                        color = component.tertiaryDarkColor.subscribeAsState().value
                    ) {
                        component.tertiaryDarkColor.value = it
                    }
                }
            }
        }




    }
}