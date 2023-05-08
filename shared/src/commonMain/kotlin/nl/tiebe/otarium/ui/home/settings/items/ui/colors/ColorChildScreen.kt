package nl.tiebe.otarium.ui.home.settings.items.ui.colors

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
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
import nl.tiebe.otarium.ui.home.settings.utils.SettingRowIconButton
import nl.tiebe.otarium.ui.home.settings.utils.SettingsColorPicker
import nl.tiebe.otarium.ui.home.settings.utils.SettingsRowToggle
import nl.tiebe.otarium.utils.dynamicColorsPossible

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
                leftText = AnnotatedString("Dynamic color scheme"),
                checked = dynamicColorScheme.value,
            ) {
                component.dynamicColorState.value = it
            }
        }

        SettingsRowToggle(
            leftText = AnnotatedString("Custom color scheme"),
            checked = customColorScheme.value,
        ) {
            component.customColorScheme.value = it
        }

        if (customColorScheme.value) {
            SettingRowIconButton(
                leftText = AnnotatedString("Reset to default"),
                icon = Icons.Default.Delete,
                onClick = {
                    component.resetColorScheme()
                }
            )

            SettingRowIconButton(
                leftText = AnnotatedString("Save color scheme"),
                icon = Icons.Default.Create,
                onClick = {
                    println(component.primaryLightColor.value.toColor())
                    component.saveColorScheme()
                }
            )

            val showLightColors = remember { mutableStateOf(false) }

            Row(modifier = Modifier.fillMaxWidth(0.95f).height(70.dp),
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
                        leftText = AnnotatedString("Primary color"),
                        color = component.primaryLightColor.subscribeAsState().value
                    ) {
                        component.primaryLightColor.value = it
                    }

                    SettingsColorPicker(
                        leftText = AnnotatedString("Secondary color"),
                        color = component.secondaryLightColor.subscribeAsState().value
                    ) {
                        component.secondaryLightColor.value = it
                    }

                    SettingsColorPicker(
                        leftText = AnnotatedString("Tertiary color"),
                        color = component.tertiaryLightColor.subscribeAsState().value
                    ) {
                        component.tertiaryLightColor.value = it
                    }
                }
            }

            val showDarkColors = remember { mutableStateOf(false) }

            Row(modifier = Modifier.fillMaxWidth(0.95f).height(70.dp),
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
                        leftText = AnnotatedString("Primary color"),
                        color = component.primaryDarkColor.subscribeAsState().value
                    ) {
                        component.primaryDarkColor.value = it
                    }

                    SettingsColorPicker(
                        leftText = AnnotatedString("Secondary color"),
                        color = component.secondaryDarkColor.subscribeAsState().value
                    ) {
                        component.secondaryDarkColor.value = it
                    }

                    SettingsColorPicker(
                        leftText = AnnotatedString("Tertiary color"),
                        color = component.tertiaryDarkColor.subscribeAsState().value
                    ) {
                        component.tertiaryDarkColor.value = it
                    }
                }
            }
        }




    }
}