package nl.tiebe.otarium.logic.home.children.settings.children.ui.children.colors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.arkivanov.decompose.value.MutableValue
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.logic.root.home.children.settings.SettingsComponent
import nl.tiebe.otarium.ui.theme.CustomTheme
import nl.tiebe.otarium.ui.theme.defaultDarkTheme
import nl.tiebe.otarium.ui.theme.defaultLightTheme
import nl.tiebe.otarium.ui.utils.colorpicker.HsvColor

val colorSchemeChanged = MutableValue(false)

interface ColorChildComponent {
    fun navigate(child: SettingsComponent.Config)

    val dynamicColorState: MutableValue<Boolean>
    val customColorScheme: MutableValue<Boolean>

    val primaryLightColor: MutableValue<HsvColor>
    val secondaryLightColor: MutableValue<HsvColor>
    val tertiaryLightColor: MutableValue<HsvColor>

    val primaryDarkColor: MutableValue<HsvColor>
    val secondaryDarkColor: MutableValue<HsvColor>
    val tertiaryDarkColor: MutableValue<HsvColor>

    fun resetColorScheme() {
        primaryLightColor.value = HsvColor.from(Color(defaultLightTheme.primary))
        secondaryLightColor.value = HsvColor.from(Color(defaultLightTheme.secondary))
        tertiaryLightColor.value = HsvColor.from(Color(defaultLightTheme.tertiary))

        primaryDarkColor.value = HsvColor.from(Color(defaultDarkTheme.primary))
        secondaryDarkColor.value = HsvColor.from(Color(defaultDarkTheme.secondary))
        tertiaryDarkColor.value = HsvColor.from(Color(defaultDarkTheme.tertiary))
    }

    fun saveColorScheme() {
        Data.customLightTheme = CustomTheme(
            primary = primaryLightColor.value.toColor().toArgb(),
            secondary = secondaryLightColor.value.toColor().toArgb(),
            tertiary = tertiaryLightColor.value.toColor().toArgb()
        )

        Data.customDarkTheme = CustomTheme(
            primary = primaryDarkColor.value.toColor().toArgb(),
            secondary = secondaryDarkColor.value.toColor().toArgb(),
            tertiary = tertiaryDarkColor.value.toColor().toArgb()
        )

        colorSchemeChanged.value = !colorSchemeChanged.value
    }
}