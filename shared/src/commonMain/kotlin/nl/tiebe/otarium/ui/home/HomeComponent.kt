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
import nl.tiebe.otarium.ui.home.grades.DefaultGradesComponent
import nl.tiebe.otarium.ui.home.settings.DefaultSettingsComponent
import nl.tiebe.otarium.ui.home.timetable.DefaultTimetableComponent
import nl.tiebe.otarium.ui.icons.CalendarTodayIcon
import nl.tiebe.otarium.ui.icons.Looks10Icon
import nl.tiebe.otarium.ui.icons.SettingsIcon
import nl.tiebe.otarium.ui.root.RootComponent

interface HomeComponent {
    val dialog: Value<ChildOverlay<MenuItem, MenuItemComponent>>

    @Parcelize
    sealed class MenuItem(val resourceId: StringResource, val icon: @Composable () -> Unit): Parcelable {
        object Timetable: MenuItem(MR.strings.agendaItem, { Icon(CalendarTodayIcon, "Timetable") })
        object Grades: MenuItem(MR.strings.gradesItem, { Icon(Looks10Icon, "Grades") })
        object Settings: MenuItem(MR.strings.settings_title, { Icon(SettingsIcon, "Settings") })
    }

    val navigateRootComponent: (RootComponent.ChildScreen) -> Unit
    fun navigate(item: MenuItem)
}

class DefaultHomeComponent(componentContext: ComponentContext, override val navigateRootComponent: (RootComponent.ChildScreen) -> Unit): HomeComponent, ComponentContext by componentContext {
    private val dialogNavigation = OverlayNavigation<HomeComponent.MenuItem>()


    override val dialog: Value<ChildOverlay<HomeComponent.MenuItem, MenuItemComponent>> = childOverlay(
        source = dialogNavigation,
        initialConfiguration = { HomeComponent.MenuItem.Timetable },
        // persistent = false, // Disable navigation state saving, if needed
        handleBackButton = false, // Close the dialog on back button press
    ) { config, componentContext ->
        when (config) {
            is HomeComponent.MenuItem.Timetable -> timetableComponent(componentContext)
            is HomeComponent.MenuItem.Grades -> gradesComponent(componentContext)
            is HomeComponent.MenuItem.Settings -> settingsComponent(componentContext)
        }
    }

    private fun timetableComponent(componentContext: ComponentContext) =
        DefaultTimetableComponent(
            componentContext = componentContext,
            navigate = ::navigate
        )

    private fun gradesComponent(componentContext: ComponentContext) =
        DefaultGradesComponent(
            componentContext = componentContext
        )

    private fun settingsComponent(componentContext: ComponentContext) =
        DefaultSettingsComponent(
            componentContext = componentContext,
            navigateRootComponent = navigateRootComponent
        )

    override fun navigate(item: HomeComponent.MenuItem) {
        dialogNavigation.activate(item)
    }


}