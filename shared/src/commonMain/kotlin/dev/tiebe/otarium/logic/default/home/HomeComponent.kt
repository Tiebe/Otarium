package dev.tiebe.otarium.logic.default.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.value.Value
import dev.tiebe.otarium.logic.default.home.children.debug.DefaultDebugComponent
import dev.tiebe.otarium.logic.default.home.children.elo.DefaultELOComponent
import dev.tiebe.otarium.logic.default.home.children.grades.DefaultGradesComponent
import dev.tiebe.otarium.logic.default.home.children.messages.DefaultMessagesComponent
import dev.tiebe.otarium.logic.default.home.children.settings.DefaultSettingsComponent
import dev.tiebe.otarium.logic.default.home.children.timetable.DefaultTimetableRootComponent
import dev.tiebe.otarium.logic.root.RootComponent
import dev.tiebe.otarium.logic.root.home.HomeComponent
import dev.tiebe.otarium.logic.root.home.MenuItems
import dev.tiebe.otarium.logic.root.home.children.debug.DebugComponent
import dev.tiebe.otarium.logic.root.home.children.elo.ELOComponent
import dev.tiebe.otarium.logic.root.home.children.grades.GradesComponent
import dev.tiebe.otarium.logic.root.home.children.messages.MessagesComponent
import dev.tiebe.otarium.logic.root.home.children.settings.SettingsComponent
import dev.tiebe.otarium.logic.root.home.children.timetable.TimetableRootComponent

open class DefaultHomeComponent(componentContext: ComponentContext, override val navigateRootComponent: (RootComponent.ChildScreen) -> Unit): HomeComponent, ComponentContext by componentContext {
    private val dialogNavigation = SlotNavigation<MenuItems>()

    override val visibleItems: List<MenuItems> = listOf(
        MenuItems.Timetable,
        MenuItems.Grades,
        MenuItems.Messages,
        MenuItems.ELO,
        MenuItems.Settings
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