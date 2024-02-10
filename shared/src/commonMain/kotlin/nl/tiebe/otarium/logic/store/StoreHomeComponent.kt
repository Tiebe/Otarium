package nl.tiebe.otarium.logic.store

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.value.Value
import nl.tiebe.otarium.logic.root.RootComponent
import nl.tiebe.otarium.logic.root.home.HomeComponent
import nl.tiebe.otarium.logic.root.home.MenuItems
import nl.tiebe.otarium.logic.root.home.children.timetable.TimetableRootComponent
import nl.tiebe.otarium.ui.home.grades.GradesComponent

class StoreHomeComponent(componentContext: ComponentContext, override val navigateRootComponent: (RootComponent.ChildScreen) -> Unit): HomeComponent, ComponentContext by componentContext {
    private val dialogNavigation = SlotNavigation<MenuItems>()

    override val visibleItems: List<MenuItems> = listOf(
        MenuItems.Timetable,
        MenuItems.Grades
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
            else -> throw RuntimeException()
        }
    }

    private fun timetableComponent(componentContext: ComponentContext): TimetableRootComponent =
        StoreTimetableRootComponent(
            componentContext = componentContext,
        )

    private fun gradesComponent(componentContext: ComponentContext): GradesComponent =
        StoreGradeComponent(
            componentContext = componentContext
        )

    override fun navigate(item: MenuItems) {
        dialogNavigation.activate(item)
    }
}