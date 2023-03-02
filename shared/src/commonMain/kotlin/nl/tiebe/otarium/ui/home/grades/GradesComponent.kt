package nl.tiebe.otarium.ui.home.grades

import com.arkivanov.decompose.ComponentContext
import nl.tiebe.otarium.ui.home.HomeComponent

interface GradesComponent {
}

class DefaultGradesComponent(
    componentContext: ComponentContext,
    navigate: (menuItem: HomeComponent.MenuItem) -> Unit
): GradesComponent, ComponentContext by componentContext {

}