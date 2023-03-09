package nl.tiebe.otarium.ui.home

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.overlay.ChildOverlay
import com.arkivanov.decompose.router.overlay.OverlayNavigation
import com.arkivanov.decompose.router.overlay.activate
import com.arkivanov.decompose.router.overlay.childOverlay
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import dev.icerock.moko.resources.StringResource
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.oldui.icons.CalendarTodayIcon
import nl.tiebe.otarium.oldui.icons.Looks10Icon
import nl.tiebe.otarium.oldui.icons.SettingsIcon
import nl.tiebe.otarium.ui.home.agenda.DefaultTimetableComponent
import nl.tiebe.otarium.ui.home.grades.DefaultGradesComponent
import nl.tiebe.otarium.ui.home.settings.DefaultSettingsComponent

interface HomeComponent {
    val dialog: Value<ChildOverlay<MenuItem, MenuItemComponent>>

    @Parcelize
    sealed class MenuItem(val resourceId: StringResource, val icon: @Composable () -> Unit): Parcelable {
        object Timetable: MenuItem(MR.strings.agendaItem, { Icon(CalendarTodayIcon, "Timetable") })
        object Grades: MenuItem(MR.strings.gradesItem, { Icon(Looks10Icon, "Grades") })
        object Settings: MenuItem(MR.strings.settings_title, { Icon(SettingsIcon, "Settings") })
    }

    fun navigate(item: MenuItem)
}

class DefaultHomeComponent(componentContext: ComponentContext): HomeComponent, ComponentContext by componentContext {
    private val dialogNavigation = OverlayNavigation<HomeComponent.MenuItem>()

    private val _dialog =
        childOverlay(
            source = dialogNavigation,
            initialConfiguration = { HomeComponent.MenuItem.Timetable },
            // persistent = false, // Disable navigation state saving, if needed
            handleBackButton = false, // Close the dialog on back button press
        ) { config, componentContext ->
            when (config) {
                is HomeComponent.MenuItem.Timetable -> timetableComponent(componentContext)
                is HomeComponent.MenuItem.Grades -> gradesComponent(componentContext)
                is HomeComponent.MenuItem.Settings -> settingsComponent(componentContext)
            } as MenuItemComponent
        }

    override val dialog: Value<ChildOverlay<HomeComponent.MenuItem, MenuItemComponent>> = _dialog

    private fun timetableComponent(componentContext: ComponentContext) =
        DefaultTimetableComponent(
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

    override fun navigate(item: HomeComponent.MenuItem) {
        dialogNavigation.activate(item)
    }


}