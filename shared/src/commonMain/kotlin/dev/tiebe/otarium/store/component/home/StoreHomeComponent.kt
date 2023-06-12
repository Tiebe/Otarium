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
import dev.tiebe.otarium.ui.home.HomeComponent
import dev.tiebe.otarium.ui.home.MenuItemComponent
import dev.tiebe.otarium.ui.home.debug.DefaultDebugComponent
import dev.tiebe.otarium.ui.home.elo.DefaultELOComponent
import dev.tiebe.otarium.ui.home.messages.DefaultMessagesComponent
import dev.tiebe.otarium.ui.root.RootComponent

class StoreHomeComponent(componentContext: ComponentContext, override val navigateRootComponent: (RootComponent.ChildScreen) -> Unit
): HomeComponent, ComponentContext by componentContext {
    private val dialogNavigation = SlotNavigation<HomeComponent.MenuItem>()

    override val visibleItems: List<HomeComponent.MenuItem> = listOf(
        HomeComponent.MenuItem.Timetable,
        HomeComponent.MenuItem.Grades,
        HomeComponent.MenuItem.Settings
    )

    override val dialog: Value<ChildSlot<HomeComponent.MenuItem, MenuItemComponent>> = childSlot<HomeComponent.MenuItem, MenuItemComponent>(
        dialogNavigation,
        "DefaultChildOverlay", { HomeComponent.MenuItem.Timetable },
        persistent = true,
        handleBackButton = false
    ) { config, componentContext ->
        when (config) {
            is HomeComponent.MenuItem.Timetable -> timetableComponent(componentContext)
            is HomeComponent.MenuItem.Grades -> gradesComponent(componentContext)
            is HomeComponent.MenuItem.Messages -> messagesComponent(componentContext)
            is HomeComponent.MenuItem.Settings -> settingsComponent(componentContext)
            is HomeComponent.MenuItem.Debug -> debugComponent(componentContext)
            is HomeComponent.MenuItem.ELO -> eloComponent(componentContext)
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

    override fun navigate(item: HomeComponent.MenuItem) {
        dialogNavigation.activate(item)
    }
}