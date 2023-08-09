package nl.tiebe.otarium.logic.home.children.settings.children.main

import nl.tiebe.otarium.logic.home.children.settings.SettingsComponent

/**
 * Interface for the main menu of the settings screen.
 */
interface MainChildComponent {
    /**
     * Navigate to the given child.
     */
    fun navigate(child: SettingsComponent.Config)

}
