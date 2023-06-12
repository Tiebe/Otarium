package dev.tiebe.otarium.ui.home.settings.items.bugs

import com.arkivanov.decompose.ComponentContext
import dev.tiebe.otarium.ui.home.settings.SettingsComponent

interface BugsChildComponent {
    fun navigate(child: SettingsComponent.Config)

}

class DefaultBugsChildComponent(
    componentContext: ComponentContext,
    private val _navigate: (child: SettingsComponent.Config) -> Unit
) : BugsChildComponent, ComponentContext by componentContext {
    override fun navigate(child: SettingsComponent.Config) {
        _navigate(child)
    }

}