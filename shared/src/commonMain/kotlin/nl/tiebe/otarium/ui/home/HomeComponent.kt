package nl.tiebe.otarium.ui.home

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import nl.tiebe.otarium.ui.home.elo.DefaultELOComponent
import nl.tiebe.otarium.ui.home.grades.DefaultGradesComponent
import nl.tiebe.otarium.ui.home.messages.DefaultMessagesComponent
import nl.tiebe.otarium.ui.home.settings.DefaultSettingsComponent
import nl.tiebe.otarium.ui.home.timetable.DefaultTimetableComponent
import nl.tiebe.otarium.ui.root.RootComponent
import nl.tiebe.otarium.utils.icons.Bottombar
import nl.tiebe.otarium.utils.icons.Icons
import nl.tiebe.otarium.utils.icons.bottombar.*

interface HomeComponent {
    val dialog: Value<ChildSlot<MenuItem, MenuItemComponent>>

    val visibleItems: List<MenuItem> get() = listOf(MenuItem.Timetable, MenuItem.Grades, MenuItem.Messages, MenuItem.ELO, MenuItem.Settings)

    @Parcelize
    sealed class MenuItem(val resourceId: StringResource, val icon: @Composable () -> Unit, val iconSelected: @Composable () -> Unit): Parcelable {
        object Timetable: MenuItem(
            MR.strings.agendaItem,
            { Icon(Icons.Bottombar.CalendarTodayOutline, "Timetable", tint = MaterialTheme.colorScheme.onPrimary) },
            { Icon(Icons.Bottombar.CalendarTodayFilled, "Timetable", tint = MaterialTheme.colorScheme.onSecondaryContainer) },
        )

        object Grades: MenuItem(
            MR.strings.gradesItem,
            { Icon(Icons.Bottombar.Box10Outline, "Grades", tint = MaterialTheme.colorScheme.onPrimary) },
            { Icon(Icons.Bottombar.Box10Filled, "Grades", tint = MaterialTheme.colorScheme.onSecondaryContainer) },
        )

        object Messages: MenuItem(
            MR.strings.messagesItem,
            { Icon(Icons.Bottombar.EmailOutline, "Messages", tint = MaterialTheme.colorScheme.onPrimary) },
            { Icon(Icons.Bottombar.EmailFilled, "Messages", tint = MaterialTheme.colorScheme.onSecondaryContainer) },
        )

        object ELO: MenuItem(
            MR.strings.eloItem,
            { Icon(Icons.Bottombar.BookOpenOutline, "ELO", tint = MaterialTheme.colorScheme.onPrimary) },
            { Icon(Icons.Bottombar.BookOpenFilled, "ELO", tint = MaterialTheme.colorScheme.onSecondaryContainer) },
        )

        object Settings: MenuItem(
            MR.strings.settingsItem,
            { Icon(Icons.Bottombar.CogOutline, "Settings", tint = MaterialTheme.colorScheme.onPrimary) },
            { Icon(Icons.Bottombar.CogFilled, "Settings", tint = MaterialTheme.colorScheme.onSecondaryContainer) },
        )

        object Debug: MenuItem(
            MR.strings.settingsItem,
            { Icon(Icons.Bottombar.Box10Outline, "Debug", tint = MaterialTheme.colorScheme.onPrimary) },
            { Icon(Icons.Bottombar.Box10Filled, "Debug", tint = MaterialTheme.colorScheme.onSecondaryContainer) },
        )
    }

    val navigateRootComponent: (RootComponent.ChildScreen) -> Unit
    fun navigate(item: MenuItem)
}

class DefaultHomeComponent(componentContext: ComponentContext, override val navigateRootComponent: (RootComponent.ChildScreen) -> Unit): HomeComponent, ComponentContext by componentContext {
    private val dialogNavigation = SlotNavigation<HomeComponent.MenuItem>()

    override val dialog: Value<ChildSlot<HomeComponent.MenuItem, MenuItemComponent>> = childSlot<HomeComponent.MenuItem, MenuItemComponent>(
        dialogNavigation,
        "HomeComponentChildOverlay",
        { HomeComponent.MenuItem.Timetable },
        persistent = true,
        handleBackButton = false
    ) { config, componentContext ->
        when (config) {
            is HomeComponent.MenuItem.Timetable -> timetableComponent(componentContext)
            is HomeComponent.MenuItem.Grades -> gradesComponent(componentContext)
            is HomeComponent.MenuItem.Messages -> messagesComponent(componentContext)
            is HomeComponent.MenuItem.ELO -> eloComponent(componentContext)
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

    private fun messagesComponent(componentContext: ComponentContext) =
        DefaultMessagesComponent(
            componentContext = componentContext
        )

    private fun eloComponent(componentContext: ComponentContext) =
        DefaultELOComponent(
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

    init {
        println("init")
    }

    override fun navigate(item: HomeComponent.MenuItem) {
        dialogNavigation.activate(item)

        clickCount = if (item == clickCount.first) {
            item to clickCount.second + 1
        } else {
            item to 0
        }

        if (clickCount.second >= 8) {
            dialogNavigation.activate(HomeComponent.MenuItem.Debug)
        }
    }
}