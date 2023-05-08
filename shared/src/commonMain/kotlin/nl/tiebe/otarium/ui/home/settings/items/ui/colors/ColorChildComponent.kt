package nl.tiebe.otarium.ui.home.settings.items.ui.colors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.ui.home.settings.SettingsComponent
import nl.tiebe.otarium.ui.theme.CustomTheme
import nl.tiebe.otarium.ui.theme.defaultDarkTheme
import nl.tiebe.otarium.ui.theme.defaultLightTheme
import nl.tiebe.otarium.ui.utils.colorpicker.HsvColor

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

        println(Data.customLightTheme)

        Data.customDarkTheme = CustomTheme(
            primary = primaryDarkColor.value.toColor().toArgb(),
            secondary = secondaryDarkColor.value.toColor().toArgb(),
            tertiary = tertiaryDarkColor.value.toColor().toArgb()
        )
    }
}

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
            if (it) customColorScheme.value = false
        }
        customColorScheme.subscribe {
            Data.customThemeEnabled = it
            if (it) dynamicColorState.value = false
        }
    }

}