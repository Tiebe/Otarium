package nl.tiebe.otarium.logic.default.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.logic.default.home.children.debug.DefaultDebugComponent
import nl.tiebe.otarium.logic.default.home.children.elo.DefaultELOComponent
import nl.tiebe.otarium.logic.default.home.children.messages.DefaultMessagesComponent
import nl.tiebe.otarium.logic.default.home.children.settings.DefaultSettingsComponent
import nl.tiebe.otarium.logic.default.home.children.timetable.DefaultTimetableRootComponent
import nl.tiebe.otarium.logic.root.RootComponent
import nl.tiebe.otarium.logic.root.home.HomeComponent
import nl.tiebe.otarium.logic.root.home.MenuItems
import nl.tiebe.otarium.logic.root.home.children.debug.DebugComponent
import nl.tiebe.otarium.logic.root.home.children.elo.ELOComponent
import nl.tiebe.otarium.logic.root.home.children.messages.MessagesComponent
import nl.tiebe.otarium.logic.root.home.children.settings.SettingsComponent
import nl.tiebe.otarium.logic.root.home.children.timetable.TimetableRootComponent
import nl.tiebe.otarium.ui.home.grades.DefaultGradesComponent
import nl.tiebe.otarium.ui.home.grades.GradesComponent

class DefaultHomeComponent(componentContext: ComponentContext, override val navigateRootComponent: (RootComponent.ChildScreen) -> Unit): HomeComponent, ComponentContext by componentContext {
    private val dialogNavigation = SlotNavigation<MenuItems>()

    override val visibleItems: List<MenuItems> = listOf(
        MenuItems.Timetable,
        MenuItems.Grades,
        MenuItems.ELO,
        MenuItems.Messages,
        MenuItems.Settings
    )

    override val dialog: Value<ChildSlot<MenuItems, HomeComponent.MenuItemComponent>> = childSlot(
        source = dialogNavigation,
        serializer = MenuItems.serializer(),
        key = "HomeComponentChildOverlay",
        initialConfiguration = { MenuItems.Timetable },
        handleBackButton = false
    ) { item, componentContext ->
        when (item) {
            MenuItems.Timetable -> timetableComponent(componentContext)
            MenuItems.Grades -> gradesComponent(componentContext)
            MenuItems.Messages -> messagesComponent(componentContext)
            MenuItems.ELO -> eloComponent(componentContext)
            MenuItems.Settings -> settingsComponent(componentContext)
            MenuItems.Debug -> debugComponent(componentContext)
        }
    }

    private fun timetableComponent(componentContext: ComponentContext): TimetableRootComponent =
        DefaultTimetableRootComponent(
            componentContext = componentContext,
        )

    private fun gradesComponent(componentContext: ComponentContext): GradesComponent =
        DefaultGradesComponent(
            componentContext = componentContext
        )

    private fun messagesComponent(componentContext: ComponentContext): MessagesComponent =
        DefaultMessagesComponent(
            componentContext = componentContext
        )

    private fun eloComponent(componentContext: ComponentContext): ELOComponent =
        DefaultELOComponent(
            componentContext = componentContext
        )

    private fun settingsComponent(componentContext: ComponentContext): SettingsComponent =
        DefaultSettingsComponent(
            componentContext = componentContext,
            navigateRootComponent = navigateRootComponent
        )

    private fun debugComponent(componentContext: ComponentContext): DebugComponent =
        DefaultDebugComponent(
            componentContext = componentContext,
            navigateRootComponent = navigateRootComponent
        )

    private var clickCount: Pair<MenuItems, Int> = MenuItems.Timetable to 0

    override fun navigate(item: MenuItems) {
        dialogNavigation.activate(item)

        clickCount = if (item == clickCount.first) {
            item to clickCount.second + 1
        } else {
            item to 0
        }

        if (clickCount.second >= 8) {
            dialogNavigation.activate(MenuItems.Debug)
        }
    }

    init {
        runBlocking {
            launch {
                Data.selectedAccount.refreshFolders()
            }
        }
    }
}