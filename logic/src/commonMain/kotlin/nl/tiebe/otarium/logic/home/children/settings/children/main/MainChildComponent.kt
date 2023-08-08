package nl.tiebe.otarium.logic.home.children.settings.children.main

import nl.tiebe.otarium.logic.root.home.children.settings.SettingsComponent

interface MainChildComponent {
    fun navigate(child: SettingsComponent.Config)

}
