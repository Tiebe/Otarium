package nl.tiebe.otarium.logic.default.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.value.Value
import nl.tiebe.otarium.logic.default.home.children.averages.DefaultAveragesComponent
import nl.tiebe.otarium.logic.default.home.children.debug.DefaultDebugComponent
import nl.tiebe.otarium.logic.default.home.children.elo.DefaultELOComponent
import nl.tiebe.otarium.logic.default.home.children.grades.DefaultGradesComponent
import nl.tiebe.otarium.logic.default.home.children.messages.DefaultMessagesComponent
import nl.tiebe.otarium.logic.default.home.children.settings.DefaultSettingsComponent
import nl.tiebe.otarium.logic.default.home.children.timetable.DefaultTimetableRootComponent
import nl.tiebe.otarium.logic.root.RootComponent
import nl.tiebe.otarium.logic.root.home.HomeComponent
import nl.tiebe.otarium.logic.root.home.MenuItems
import nl.tiebe.otarium.logic.root.home.children.averages.AveragesComponent
import nl.tiebe.otarium.logic.root.home.children.debug.DebugComponent
import nl.tiebe.otarium.logic.root.home.children.elo.ELOComponent
import nl.tiebe.otarium.logic.root.home.children.grades.GradesComponent
import nl.tiebe.otarium.logic.root.home.children.messages.MessagesComponent
import nl.tiebe.otarium.logic.root.home.children.settings.SettingsComponent
import nl.tiebe.otarium.logic.root.home.children.timetable.TimetableRootComponent

open class DefaultHomeComponent(componentContext: ComponentContext, override val navigateRootComponent: (RootComponent.ChildScreen) -> Unit): HomeComponent, ComponentContext by componentContext {
    private val dialogNavigation = SlotNavigation<MenuItems>()

    override val visibleItems: List<MenuItems> = listOf(
        MenuItems.Timetable,
        MenuItems.Grades,
        MenuItems.Averages,
        MenuItems.ELO,
        MenuItems.Messages,
    )

    override val dialog: Value<ChildSlot<MenuItems, HomeComponent.MenuItemComponent>> = childSlot<MenuItems, HomeComponent.MenuItemComponent>(
        dialogNavigation,
        "HomeComponentChildOverlay",
        { MenuItems.Timetable },
        persistent = true,
        handleBackButton = false
    ) { config, componentContext ->
        when (config) {
            is MenuItems.Timetable -> timetableComponent(componentContext)
            is MenuItems.Grades -> gradesComponent(componentContext)
            is MenuItems.Averages -> averagesComponent(componentContext)
            is MenuItems.Messages -> messagesComponent(componentContext)
            is MenuItems.ELO -> eloComponent(componentContext)
            is MenuItems.Settings -> settingsComponent(componentContext)
            is MenuItems.Debug -> debugComponent(componentContext)
        }
    }

    open fun timetableComponent(componentContext: ComponentContext): TimetableRootComponent =
        DefaultTimetableRootComponent(
            componentContext = componentContext,
        )

    open fun gradesComponent(componentContext: ComponentContext): GradesComponent =
        DefaultGradesComponent(
            componentContext = componentContext
        )

    open fun averagesComponent(componentContext: ComponentContext): AveragesComponent =
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