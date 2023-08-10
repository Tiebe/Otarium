package nl.tiebe.otarium.logic.magister.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.value.Value
import nl.tiebe.otarium.logic.RootComponent
import nl.tiebe.otarium.logic.default.home.children.averages.DefaultAveragesComponent
import nl.tiebe.otarium.logic.default.home.children.debug.DefaultDebugComponent
import nl.tiebe.otarium.logic.default.home.children.elo.DefaultELOComponent
import nl.tiebe.otarium.logic.magister.home.children.grades.DefaultGradesComponent
import nl.tiebe.otarium.logic.default.home.children.messages.DefaultMessagesComponent
import nl.tiebe.otarium.logic.default.home.children.settings.DefaultSettingsComponent
import nl.tiebe.otarium.logic.home.HomeComponent
import nl.tiebe.otarium.logic.home.MenuItem
import nl.tiebe.otarium.logic.magister.home.children.timetable.DefaultTimetableComponent
import nl.tiebe.otarium.logic.root.home.MenuItems
import nl.tiebe.otarium.logic.root.home.children.averages.AveragesComponent
import nl.tiebe.otarium.logic.root.home.children.debug.DebugComponent
import nl.tiebe.otarium.logic.root.home.children.elo.ELOComponent
import nl.tiebe.otarium.logic.root.home.children.grades.GradesComponent
import nl.tiebe.otarium.logic.root.home.children.messages.MessagesComponent
import nl.tiebe.otarium.logic.root.home.children.settings.SettingsComponent

class DefaultHomeComponent(componentContext: ComponentContext, override val rootComponent: RootComponent): HomeComponent, ComponentContext by componentContext {
    override val navigation = SlotNavigation<MenuItem>()

    override val menuItems = listOf(
        MenuItem("Timetable", {}, {}),
        MenuItem("Grades", {}, {}),
        MenuItem("Averages", {}, {}),
        MenuItem("Messages", {}, {}),
        MenuItem("ELO", {}, {}),
        MenuItem("Settings", {}, {}),
    )

    val dialog: Value<ChildSlot<MenuItem, HomeComponent.MenuItemComponent>> = childSlot<MenuItem, HomeComponent.MenuItemComponent>(
        navigation,
        "HomeComponentChildOverlay",
        { menuItems[0] },
        persistent = true,
        handleBackButton = false
    ) { config, componentContext ->
        when (config) {
            menuItems[0] -> timetableComponent(componentContext)
            menuItems[1] -> gradesComponent(componentContext)
            menuItems[2] -> averagesComponent(componentContext)
            menuItems[3] -> messagesComponent(componentContext)
            menuItems[4] -> eloComponent(componentContext)
            menuItems[5] -> settingsComponent(componentContext)
            else -> { debugComponent(componentContext) }
        }
    }

    fun timetableComponent(componentContext: ComponentContext) =
        DefaultTimetableComponent(
            componentContext = componentContext,
        )

    fun gradesComponent(componentContext: ComponentContext): GradesComponent =
        DefaultGradesComponent(
            componentContext = componentContext
        )

    fun averagesComponent(componentContext: ComponentContext): AveragesComponent =
        DefaultAveragesComponent(
            componentContext = componentContext
        )

    open fun messagesComponent(componentContext: ComponentContext): MessagesComponent =
        DefaultMessagesComponent(
            componentContext = componentContext
        )

    open fun eloComponent(componentContext: ComponentContext): ELOComponent =
        DefaultELOComponent(
            componentContext = componentContext
        )

    open fun settingsComponent(componentContext: ComponentContext): SettingsComponent =
        DefaultSettingsComponent(
            componentContext = componentContext,
            navigateRootComponent = navigateRootComponent
        )

    open fun debugComponent(componentContext: ComponentContext): DebugComponent =
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
}