package dev.tiebe.otarium.ui.home.settings.items.ads

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import dev.tiebe.otarium.Data
import dev.tiebe.otarium.ui.home.adsShown
import dev.tiebe.otarium.ui.home.settings.SettingsComponent
import dev.tiebe.otarium.ui.root.componentCoroutineScope

interface AdsChildComponent {
    fun navigate(child: SettingsComponent.Config)
    val scope: CoroutineScope

    fun changeAdsState(state: Boolean) {
        Data.showAds = state
        adsShown.value = state
    }

    fun changeAgeOfConsent(state: Boolean) {
        Data.ageOfConsent = state

        scope.launch {
            adsShown.value = false
            delay(500)
            adsShown.value = Data.showAds
        }
    }

}

class DefaultAdsChildComponent(
    componentContext: ComponentContext,
    private val _navigate: (child: SettingsComponent.Config) -> Unit
) : AdsChildComponent, ComponentContext by componentContext {
    override fun navigate(child: SettingsComponent.Config) {
        _navigate(child)
    }

    override val scope: CoroutineScope = componentCoroutineScope()

}