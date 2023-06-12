package dev.tiebe.otarium.logic.root.home.children.settings.children.main

import dev.tiebe.otarium.logic.root.home.children.settings.SettingsComponent

interface MainChildComponent {
    fun navigate(child: SettingsComponent.Config)

}
