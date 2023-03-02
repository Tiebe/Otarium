package nl.tiebe.otarium.ui.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.overlay.ChildOverlay
import com.arkivanov.decompose.router.overlay.OverlayNavigation
import com.arkivanov.decompose.router.overlay.activate
import com.arkivanov.decompose.router.overlay.childOverlay
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import nl.tiebe.otarium.ui.home.agenda.DefaultAgendaComponent
import nl.tiebe.otarium.ui.home.grades.DefaultGradesComponent
import nl.tiebe.otarium.ui.home.settings.DefaultSettingsComponent

interface HomeComponent {
    val dialog: Value<ChildOverlay<*, ComponentContext>>

    @Parcelize
    sealed interface MenuItem: Parcelable {
        object Agenda: MenuItem
        object Grades: MenuItem
        object Settings: MenuItem
    }
}

class DefaultHomeComponent(componentContext: ComponentContext): HomeComponent, ComponentContext by componentContext {
    private val dialogNavigation = OverlayNavigation<HomeComponent.MenuItem>()

    private val _dialog =
        childOverlay(
            source = dialogNavigation,
            // persistent = false, // Disable navigation state saving, if needed
            handleBackButton = false, // Close the dialog on back button press
        ) { config, componentContext ->
            when (config) {
                is HomeComponent.MenuItem.Agenda -> agendaComponent(componentContext)
                is HomeComponent.MenuItem.Grades -> gradesComponent(componentContext)
                is HomeComponent.MenuItem.Settings -> settingsComponent(componentContext)
            }
        }

    override val dialog: Value<ChildOverlay<*, ComponentContext>> = _dialog

    private fun agendaComponent(componentContext: ComponentContext) =
        DefaultAgendaComponent(
            componentContext = componentContext,
            navigate = ::navigate
        )

    private fun gradesComponent(componentContext: ComponentContext) =
        DefaultGradesComponent(
            componentContext = componentContext,
            navigate = ::navigate
        )

    private fun settingsComponent(componentContext: ComponentContext) =
        DefaultSettingsComponent(
            componentContext = componentContext,
            navigate = ::navigate
        )

    private fun navigate(menuItem: HomeComponent.MenuItem) {
        dialogNavigation.activate(menuItem)
    }


}