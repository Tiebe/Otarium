package dev.tiebe.otarium.logic.default.home.children.settings.children.main

import com.arkivanov.decompose.ComponentContext
import dev.tiebe.otarium.logic.home.children.settings.SettingsComponent

class DefaultMainChildComponent(
    componentContext: ComponentContext,
    private val _navigate: (child: SettingsComponent.Config) -> Unit
) : MainChildComponent, ComponentContext by componentContext {

    override fun navigate(child: SettingsComponent.Config) {
        _navigate(child)
    }

}