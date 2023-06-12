package dev.tiebe.otarium.store.component.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.value.Value
import dev.tiebe.otarium.store.component.home.children.StoreTimetableComponent
import dev.tiebe.otarium.store.component.home.children.grades.StoreGradeComponent
import dev.tiebe.otarium.store.component.home.children.settings.StoreSettingsComponent
import dev.tiebe.otarium.logic.home.children.debug.DefaultDebugComponent
import dev.tiebe.otarium.logic.home.children.elo.DefaultELOComponent
import dev.tiebe.otarium.logic.home.children.messages.DefaultMessagesComponent
import dev.tiebe.otarium.logic.default.RootComponent

class StoreHomeComponent(componentContext: ComponentContext, override val navigateRootComponent: (RootComponent.ChildScreen) -> Unit
): _root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent, ComponentContext by componentContext {
    private val dialogNavigation = SlotNavigation<_root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent.MenuItem>()

    override val visibleItems: List<_root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent.MenuItem> = listOf(
        _root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent.MenuItem.Timetable,
        _root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent.MenuItem.Grades,
        _root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent.MenuItem.Settings
    )

    override val dialog: Value<ChildSlot<_root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent.MenuItem, _root_ide_package_.dev.tiebe.otarium.logic.default.home.MenuItemComponent>> = childSlot<_root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent.MenuItem, _root_ide_package_.dev.tiebe.otarium.logic.default.home.MenuItemComponent>(
        dialogNavigation,
        "DefaultChildOverlay", { _root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent.MenuItem.Timetable },
        persistent = true,
        handleBackButton = false
    ) { config, componentContext ->
        when (config) {
            is _root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent.MenuItem.Timetable -> timetableComponent(componentContext)
            is _root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent.MenuItem.Grades -> gradesComponent(componentContext)
            is _root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent.MenuItem.Messages -> messagesComponent(componentContext)
            is _root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent.MenuItem.Settings -> settingsComponent(componentContext)
            is _root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent.MenuItem.Debug -> debugComponent(componentContext)
            is _root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent.MenuItem.ELO -> eloComponent(componentContext)
        }
    }

    private fun timetableComponent(componentContext: ComponentContext) =
        StoreTimetableComponent(
            componentContext = componentContext,
        )


    private fun gradesComponent(componentContext: ComponentContext) =
        StoreGradeComponent(
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
        StoreSettingsComponent(
            componentContext = componentContext,
            navigateRootComponent = navigateRootComponent
        )

    private fun debugComponent(componentContext: ComponentContext) =
        DefaultDebugComponent(
            componentContext = componentContext,
            navigateRootComponent = navigateRootComponent
        )

    override fun navigate(item: _root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent.MenuItem) {
        dialogNavigation.activate(item)
    }
}