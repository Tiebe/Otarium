package nl.tiebe.otarium.ui.home.settings.items.ui

import com.arkivanov.decompose.ComponentContext
import nl.tiebe.otarium.ui.home.settings.SettingsComponent

interface UIChildComponent {
    fun navigate(child: SettingsComponent.Config)

}

class DefaultUIChildComponent(
    componentContext: ComponentContext,
    private val _navigate: (child: SettingsComponent.Config) -> Unit
) : UIChildComponent, ComponentContext by componentContext {
    override fun navigate(child: SettingsComponent.Config) {
        _navigate(child)
    }

}