package nl.tiebe.otarium.ui.home.grades

import com.arkivanov.decompose.ComponentContext
import nl.tiebe.otarium.ui.home.HomeComponent
import nl.tiebe.otarium.ui.home.MenuItemComponent

interface GradesComponent : MenuItemComponent {
}

class DefaultGradesComponent(
    componentContext: ComponentContext,
    navigate: (menuItem: HomeComponent.MenuItem) -> Unit
): GradesComponent, ComponentContext by componentContext {

}