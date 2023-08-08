package nl.tiebe.otarium.logic.home

import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import nl.tiebe.otarium.logic.RootComponent

interface HomeComponent {
    /** The root component object. Should probably do this using DI one day. */
    val rootComponent: RootComponent

    /** Slot navigation object. */
    val navigation: SlotNavigation<MenuItem>

    /** A list of supported and visible menu items. */
    val menuItems: List<MenuItem>

    /**
     * Open the specified menu item.
     *
     * @param item The menu item.
     */
    fun navigate(item: MenuItem) {
        navigation.activate(item)
    }

    /** Menu items should extend this interface. */
    interface MenuItemComponent
}