package nl.tiebe.otarium.logic.home.children.settings.children.ui.children.colors

import com.arkivanov.decompose.value.MutableValue
import com.github.ajalt.colormath.model.RGB
import kotlinx.serialization.Serializable

@Serializable
data class CustomTheme(
    val primary: Int,
    val secondary: Int,
    val tertiary: Int
)

val red = RGB("0xFFC40808")
val green = RGB("0xFF1FC43B")

val defaultLightTheme = MutableValue(CustomTheme(
    RGB("0xFF6ACBF0").toRGBInt().toRGBA().toInt(),
    RGB("0xFF1FC43B").toRGBInt().toRGBA().toInt(),
    RGB("0xFFC40808").toRGBInt().toRGBA().toInt()
))

val defaultDarkTheme = MutableValue(CustomTheme(
    RGB("0xFF0F86E4").toRGBInt().toRGBA().toInt(),
    RGB("0xFF1FC43B").toRGBInt().toRGBA().toInt(),
    RGB("0xFFC40808").toRGBInt().toRGBA().toInt()
))