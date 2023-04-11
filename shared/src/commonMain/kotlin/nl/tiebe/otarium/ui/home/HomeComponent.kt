package nl.tiebe.otarium.ui.home

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import dev.icerock.moko.resources.StringResource
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.ui.home.debug.DefaultDebugComponent
import nl.tiebe.otarium.ui.home.grades.DefaultGradesComponent
import nl.tiebe.otarium.ui.home.settings.DefaultSettingsComponent
import nl.tiebe.otarium.ui.home.timetable.DefaultTimetableComponent
import nl.tiebe.otarium.ui.icons.CalendarTodayIcon
import nl.tiebe.otarium.ui.icons.Looks10Icon
import nl.tiebe.otarium.ui.icons.SettingsIcon
import nl.tiebe.otarium.ui.root.RootComponent

interface HomeComponent {
    val dialog: Value<ChildSlot<MenuItem, MenuItemComponent>>

    @Parcelize
    sealed class MenuItem(val resourceId: StringResource, val icon: @Composable () -> Unit): Parcelable {
        object Timetable: MenuItem(MR.strings.agendaItem, { Icon(CalendarTodayIcon, "Timetable") })
        object Grades: MenuItem(MR.strings.gradesItem, { Icon(Looks10Icon, "Grades") })
        object Settings: MenuItem(MR.strings.settings_title, { Icon(SettingsIcon, "Settings") })
        object Debug: MenuItem(MR.strings.settings_title, { Icon(SettingsIcon, "Debug") })
    }

    val navigateRootComponent: (RootComponent.ChildScreen) -> Unit
    fun navigate(item: MenuItem)
}

class DefaultHomeComponent(componentContext: ComponentContext, override val navigateRootComponent: (RootComponent.ChildScreen) -> Unit): HomeComponent, ComponentContext by componentContext {
    private val dialogNavigation = SlotNavigation<HomeComponent.MenuItem>()

    override val dialog: Value<ChildSlot<HomeComponent.MenuItem, MenuItemComponent>> = childSlot<HomeComponent.MenuItem, MenuItemComponent>(
        dialogNavigation,
        "DefaultChildOverlay", { HomeComponent.MenuItem.Timetable },
        persistent = true,
        handleBackButton = false
    ) { config, componentContext ->
        when (config) {
            is HomeComponent.MenuItem.Timetable -> timetableComponent(componentContext)
            is HomeComponent.MenuItem.Grades -> gradesComponent(componentContext)
            is HomeComponent.MenuItem.Settings -> settingsComponent(componentContext)
            is HomeComponent.MenuItem.Debug -> debugComponent(componentContext)
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

    private fun debugComponent(componentContext: ComponentContext) =
        DefaultDebugComponent(
            componentContext = componentContext,
            navigateRootComponent = navigateRootComponent
        )

    private var clickCount: Pair<HomeComponent.MenuItem, Int> = HomeComponent.MenuItem.Timetable to 0

    override fun navigate(item: HomeComponent.MenuItem) {
        dialogNavigation.activate(item)

        clickCount = if (item == clickCount.first) {
            item to clickCount.second + 1
        } else {
            item to 0
        }

        if (clickCount.second >= 5) {
            dialogNavigation.activate(HomeComponent.MenuItem.Debug)
        }
    }




}