package dev.tiebe.otarium.ui.home.settings.items.main

import com.arkivanov.decompose.ComponentContext
import dev.tiebe.otarium.ui.home.settings.SettingsComponent

interface MainChildComponent {
    fun navigate(child: SettingsComponent.Config)

}

class DefaultMainChildComponent(
    componentContext: ComponentContext,
    private val _navigate: (child: SettingsComponent.Config) -> Unit
) : MainChildComponent, ComponentContext by componentContext {

    override fun navigate(child: SettingsComponent.Config) {
        _navigate(child)
    }

}