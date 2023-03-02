package nl.tiebe.otarium.ui.home.settings

import com.arkivanov.decompose.ComponentContext
import nl.tiebe.otarium.ui.home.HomeComponent
import nl.tiebe.otarium.ui.home.MenuItemComponent

interface SettingsComponent: MenuItemComponent {

}

class DefaultSettingsComponent(
    componentContext: ComponentContext,
    navigate: (menuItem: HomeComponent.MenuItem) -> Unit
): SettingsComponent, ComponentContext by componentContext {
}