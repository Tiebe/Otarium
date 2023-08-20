package nl.tiebe.otarium.androidApp.ui.home.settings.items.ui.colors

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.androidApp.dynamicColorsPossible
import nl.tiebe.otarium.androidApp.ui.home.settings.utils.SettingRowIconButton
import nl.tiebe.otarium.androidApp.ui.home.settings.utils.SettingsRowToggle
import nl.tiebe.otarium.logic.root.home.children.settings.children.ui.children.colors.ColorChildComponent
import nl.tiebe.otarium.utils.OtariumIcons
import nl.tiebe.otarium.utils.otariumicons.ContentSave

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
                leftText = AnnotatedString(stringResource(MR.strings.color_dynamic.resourceId)),
                checked = dynamicColorScheme.value,
                rowClickable = true
            ) {
                component.dynamicColorState.value = it
            }
        }

        SettingsRowToggle(
            leftText = AnnotatedString(stringResource(MR.strings.color_custom.resourceId)),
            checked = customColorScheme.value,
            rowClickable = true
        ) {
            component.customColorScheme.value = it
        }

        if (customColorScheme.value) {
            SettingRowIconButton(
                leftText = AnnotatedString(stringResource(MR.strings.color_reset.resourceId)),
                icon = Icons.Default.Refresh,
                rowClickable = false,
                onClick = {
                    component.resetColorScheme()
                }
            )

            SettingRowIconButton(
                leftText = AnnotatedString(stringResource(MR.strings.color_save.resourceId)),
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
        }




    }
}