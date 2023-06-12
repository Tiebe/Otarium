package dev.tiebe.otarium.logic.default.home.children.settings.children.ui.children.colors

import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import dev.tiebe.otarium.Data
import dev.tiebe.otarium.logic.root.home.children.settings.SettingsComponent
import dev.tiebe.otarium.logic.root.home.children.settings.children.ui.children.colors.ColorChildComponent
import dev.tiebe.otarium.ui.utils.colorpicker.HsvColor

val colorSchemeChanged = MutableValue(false)

class DefaultColorChildComponent(
    componentContext: ComponentContext,
    private val _navigate: (child: SettingsComponent.Config) -> Unit
) : ColorChildComponent, ComponentContext by componentContext {
    override val dynamicColorState: MutableValue<Boolean> = MutableValue(Data.dynamicTheme)
    override val customColorScheme: MutableValue<Boolean> = MutableValue(Data.customThemeEnabled)

    private val customLightTheme = Data.customLightTheme
    private val customDarkTheme = Data.customDarkTheme

    override val primaryLightColor: MutableValue<HsvColor> = MutableValue(HsvColor.from(Color(customLightTheme.primary)))
    override val secondaryLightColor: MutableValue<HsvColor> = MutableValue(HsvColor.from(Color(customLightTheme.secondary)))
    override val tertiaryLightColor: MutableValue<HsvColor> = MutableValue(HsvColor.from(Color(customLightTheme.tertiary)))

    override val primaryDarkColor: MutableValue<HsvColor> = MutableValue(HsvColor.from(Color(customDarkTheme.primary)))
    override val secondaryDarkColor: MutableValue<HsvColor> = MutableValue(HsvColor.from(Color(customDarkTheme.secondary)))
    override val tertiaryDarkColor: MutableValue<HsvColor> = MutableValue(HsvColor.from(Color(customDarkTheme.tertiary)))



    override fun navigate(child: SettingsComponent.Config) {
        _navigate(child)
    }

    init {
        dynamicColorState.subscribe {
            Data.dynamicTheme = it
            colorSchemeChanged.value = !colorSchemeChanged.value
            if (it) customColorScheme.value = false
        }
        customColorScheme.subscribe {
            Data.customThemeEnabled = it
            colorSchemeChanged.value = !colorSchemeChanged.value
            if (it) dynamicColorState.value = false
        }
    }

}