package nl.tiebe.otarium.ui.home.agenda

import com.arkivanov.decompose.ComponentContext
import nl.tiebe.otarium.ui.home.HomeComponent

interface AgendaComponent {
}

class DefaultAgendaComponent(
    componentContext: ComponentContext,
    navigate: (menuItem: HomeComponent.MenuItem) -> Unit
): AgendaComponent, ComponentContext by componentContext {
}