package nl.tiebe.otarium.logic.home.children.settings.children.ui.children.colors

import nl.tiebe.otarium.logic.home.children.settings.SettingsComponent

/**
 * Interface for the color menu of the settings screen.
 */
interface ColorChildComponent {
    /**
     * Navigate to the given child.
     */
    fun navigate(child: SettingsComponent.Config)

    /**
     * Should reset the color scheme to the default color scheme.
     */
    fun resetCustomColorSchemes()

    /**
     * Gets the current custom color schemes.
     *
     * @return A pair of the light and dark color schemes.
     */
    fun getCustomColorScheme(): Pair<CustomTheme, CustomTheme>

    /**
     * Sets the custom color schemes.
     *
     * @param customLightTheme The light color scheme.
     * @param customDarkTheme The dark color scheme.
     */
    fun setCustomColorScheme(customLightTheme: CustomTheme, customDarkTheme: CustomTheme)
}