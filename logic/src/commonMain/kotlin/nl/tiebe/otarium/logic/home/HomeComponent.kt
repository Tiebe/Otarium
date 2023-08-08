package nl.tiebe.otarium.logic.home

import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import nl.tiebe.otarium.logic.RootComponent

interface HomeComponent {
    val rootComponent: RootComponent
    val navigation: SlotNavigation<MenuItem>

    val menuItems: List<MenuItem>

    fun navigate(item: MenuItem) {
        navigation.activate(item)
    }

    interface MenuItemComponent
}