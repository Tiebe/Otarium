package dev.tiebe.otarium.ui.home

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
import dev.tiebe.otarium.MR
import dev.tiebe.otarium.ui.home.debug.DefaultDebugComponent
import dev.tiebe.otarium.ui.home.elo.DefaultELOComponent
import dev.tiebe.otarium.ui.home.grades.DefaultGradesComponent
import dev.tiebe.otarium.ui.home.messages.DefaultMessagesComponent
import dev.tiebe.otarium.ui.home.settings.DefaultSettingsComponent
import dev.tiebe.otarium.ui.home.timetable.DefaultTimetableRootComponent
import dev.tiebe.otarium.ui.root.RootComponent
import dev.tiebe.otarium.utils.OtariumIcons
import dev.tiebe.otarium.utils.otariumicons.Bottombar
import dev.tiebe.otarium.utils.otariumicons.bottombar.*

interface HomeComponent {
    val dialog: Value<ChildSlot<MenuItem, MenuItemComponent>>

    val visibleItems: List<MenuItem> get() = listOf(MenuItem.Timetable, MenuItem.Grades, MenuItem.Messages, MenuItem.ELO, MenuItem.Settings)

    @Parcelize
    sealed class MenuItem(val resourceId: StringResource, val icon: @Composable () -> Unit, val iconSelected: @Composable () -> Unit): Parcelable {
        object Timetable: MenuItem(
            MR.strings.agendaItem,
            { Icon(OtariumIcons.Bottombar.CalendarTodayOutline, "Timetable") },
            { Icon(OtariumIcons.Bottombar.CalendarTodayFilled, "Timetable") },
        )

        object Grades: MenuItem(
            MR.strings.gradesItem,
            { Icon(OtariumIcons.Bottombar.Box10Outline, "Grades") },
            { Icon(OtariumIcons.Bottombar.Box10Filled, "Grades") },
        )

        object Messages: MenuItem(
            MR.strings.messagesItem,
            { Icon(OtariumIcons.Bottombar.EmailOutline, "Messages") },
            { Icon(OtariumIcons.Bottombar.EmailFilled, "Messages") },
        )

        object ELO: MenuItem(
            MR.strings.eloItem,
            { Icon(OtariumIcons.Bottombar.BookOpenOutline, "ELO") },
            { Icon(OtariumIcons.Bottombar.BookOpenFilled, "ELO") },
        )

        object Settings: MenuItem(
            MR.strings.settingsItem,
            { Icon(OtariumIcons.Bottombar.CogOutline, "Settings") },
            { Icon(OtariumIcons.Bottombar.CogFilled, "Settings") },
        )

        object Debug: MenuItem(
            MR.strings.settingsItem,
            { Icon(OtariumIcons.Bottombar.Box10Outline, "Debug") },
            { Icon(OtariumIcons.Bottombar.Box10Filled, "Debug") },
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
        DefaultTimetableRootComponent(
            componentContext = componentContext,
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