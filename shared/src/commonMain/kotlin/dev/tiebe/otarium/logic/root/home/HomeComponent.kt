package dev.tiebe.otarium.logic.root.home

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.tiebe.otarium.logic.root.RootComponent

interface HomeComponent {
    val dialog: Value<ChildSlot<MenuItems, MenuItemComponent>>

    val visibleItems: List<MenuItems> /*listOf(
        MenuItems.Timetable,
        MenuItems.Grades,
        MenuItems.Messages,
        MenuItems.ELO,
        MenuItems.Settings
    )*/

    val navigateRootComponent: (RootComponent.ChildScreen) -> Unit
    fun navigate(item: MenuItems)

    interface MenuItemComponent
}