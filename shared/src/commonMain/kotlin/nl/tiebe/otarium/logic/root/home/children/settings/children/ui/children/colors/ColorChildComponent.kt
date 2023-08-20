package nl.tiebe.otarium.logic.root.home.children.settings.children.ui.children.colors

import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.value.MutableValue
import nl.tiebe.otarium.logic.root.home.children.settings.SettingsComponent
import nl.tiebe.otarium.utils.HsvColor

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
        primaryLightColor.value = HsvColor.from((Color.LightGray))
        secondaryLightColor.value = HsvColor.from((Color.LightGray))
        tertiaryLightColor.value = HsvColor.from((Color.LightGray))

        primaryDarkColor.value = HsvColor.from((Color.LightGray))
        secondaryDarkColor.value = HsvColor.from((Color.LightGray))
        tertiaryDarkColor.value = HsvColor.from((Color.LightGray))
    }

    fun saveColorScheme() {

        colorSchemeChanged.value = !colorSchemeChanged.value
    }
}