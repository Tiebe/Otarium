package nl.tiebe.otarium.ui.home.settings.items.ads

import com.arkivanov.decompose.ComponentContext
import nl.tiebe.otarium.ui.home.settings.SettingsComponent

interface AdsChildComponent {
    fun navigate(child: SettingsComponent.Config)

}

class DefaultAdsChildComponent(
    componentContext: ComponentContext,
    private val _navigate: (child: SettingsComponent.Config) -> Unit
) : AdsChildComponent, ComponentContext by componentContext {
    override fun navigate(child: SettingsComponent.Config) {
        _navigate(child)
    }

}