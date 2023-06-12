package dev.tiebe.otarium.logic.default.home

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
import dev.tiebe.otarium.logic.home.children.debug.DefaultDebugComponent
import dev.tiebe.otarium.logic.home.children.elo.DefaultELOComponent
import dev.tiebe.otarium.logic.home.children.grades.DefaultGradesComponent
import dev.tiebe.otarium.logic.home.children.messages.DefaultMessagesComponent
import dev.tiebe.otarium.logic.home.children.settings.DefaultSettingsComponent
import dev.tiebe.otarium.logic.home.children.timetable.DefaultTimetableRootComponent
import dev.tiebe.otarium.logic.default.RootComponent
import dev.tiebe.otarium.utils.OtariumIcons
import dev.tiebe.otarium.utils.otariumicons.Bottombar
import dev.tiebe.otarium.utils.otariumicons.bottombar.*

class DefaultHomeComponent(componentContext: ComponentContext, override val navigateRootComponent: (RootComponent.ChildScreen) -> Unit): dev.tiebe.otarium.logic.default.home.HomeComponent, ComponentContext by componentContext {
    private val dialogNavigation = SlotNavigation<dev.tiebe.otarium.logic.default.home.HomeComponent.MenuItem>()

    override val dialog: Value<ChildSlot<_root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent.MenuItem, _root_ide_package_.dev.tiebe.otarium.logic.default.home.MenuItemComponent>> = childSlot<_root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent.MenuItem, _root_ide_package_.dev.tiebe.otarium.logic.default.home.MenuItemComponent>(
        dialogNavigation,
        "HomeComponentChildOverlay",
        { _root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent.MenuItem.Timetable },
        persistent = true,
        handleBackButton = false
    ) { config, componentContext ->
        when (config) {
            is _root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent.MenuItem.Timetable -> timetableComponent(componentContext)
            is _root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent.MenuItem.Grades -> gradesComponent(componentContext)
            is HomeComponent.MenuItem.Messages -> messagesComponent(componentContext)
            is _root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent.MenuItem.ELO -> eloComponent(componentContext)
            is _root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent.MenuItem.Settings -> settingsComponent(componentContext)
            is _root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent.MenuItem.Debug -> debugComponent(componentContext)
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

    private var clickCount: Pair<_root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent.MenuItem, Int> = _root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent.MenuItem.Timetable to 0

    init {
        println("init")
    }

    override fun navigate(item: _root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent.MenuItem) {
        dialogNavigation.activate(item)

        clickCount = if (item == clickCount.first) {
            item to clickCount.second + 1
        } else {
            item to 0
        }

        if (clickCount.second >= 8) {
            dialogNavigation.activate(_root_ide_package_.dev.tiebe.otarium.logic.default.home.HomeComponent.MenuItem.Debug)
        }
    }
}

interface MenuItemComponent