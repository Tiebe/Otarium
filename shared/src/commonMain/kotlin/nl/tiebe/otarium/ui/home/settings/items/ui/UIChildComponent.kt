package nl.tiebe.otarium.ui.home.settings.items.ui

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.ui.home.settings.SettingsComponent

interface UIChildComponent {
    fun navigate(child: SettingsComponent.Config)

    val showCancelledLessons: Value<Boolean>

    fun showCancelledLessons(value: Boolean)

}

class DefaultUIChildComponent(
    componentContext: ComponentContext,
    private val _navigate: (child: SettingsComponent.Config) -> Unit
) : UIChildComponent, ComponentContext by componentContext {
    override fun navigate(child: SettingsComponent.Config) {
        _navigate(child)
    }

    override val showCancelledLessons: MutableValue<Boolean> = MutableValue(Data.showCancelledLessons)
    override fun showCancelledLessons(value: Boolean) {
        Data.showCancelledLessons = value
        showCancelledLessons.value = value
    }

}