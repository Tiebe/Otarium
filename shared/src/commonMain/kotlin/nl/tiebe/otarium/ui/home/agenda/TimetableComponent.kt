package nl.tiebe.otarium.ui.home.agenda

import com.arkivanov.decompose.ComponentContext
import nl.tiebe.otarium.ui.home.HomeComponent
import nl.tiebe.otarium.ui.home.MenuItemComponent

interface TimetableComponent : MenuItemComponent {
}

class DefaultTimetableComponent(
    componentContext: ComponentContext,
    navigate: (menuItem: HomeComponent.MenuItem) -> Unit
): TimetableComponent, ComponentContext by componentContext {
}