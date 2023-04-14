package nl.tiebe.otarium.ui.home.settings.items.ui.colors

import com.arkivanov.decompose.ComponentContext
import nl.tiebe.otarium.ui.home.settings.SettingsComponent

interface ColorChildComponent {
    fun navigate(child: SettingsComponent.Config)

}

class DefaultColorChildComponent(
    componentContext: ComponentContext,
    private val _navigate: (child: SettingsComponent.Config) -> Unit
) : ColorChildComponent, ComponentContext by componentContext {
    override fun navigate(child: SettingsComponent.Config) {
        _navigate(child)
    }

}