package nl.tiebe.otarium.logic.root.home

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import nl.tiebe.otarium.logic.root.RootComponent

interface HomeComponent {
    val dialog: Value<ChildSlot<MenuItems, MenuItemComponent>>

    val visibleItems: List<MenuItems>

    val navigateRootComponent: (RootComponent.ChildScreen) -> Unit
    fun navigate(item: MenuItems)

    interface MenuItemComponent
}