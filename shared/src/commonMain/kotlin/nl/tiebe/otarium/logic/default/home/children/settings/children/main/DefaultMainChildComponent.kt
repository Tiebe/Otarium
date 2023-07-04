package nl.tiebe.otarium.logic.default.home.children.settings.children.main

import com.arkivanov.decompose.ComponentContext
import nl.tiebe.otarium.logic.root.home.children.settings.SettingsComponent
import nl.tiebe.otarium.logic.root.home.children.settings.children.main.MainChildComponent

class DefaultMainChildComponent(
    componentContext: ComponentContext,
    private val _navigate: (child: SettingsComponent.Config) -> Unit
) : MainChildComponent, ComponentContext by componentContext {

    override fun navigate(child: SettingsComponent.Config) {
        _navigate(child)
    }

}