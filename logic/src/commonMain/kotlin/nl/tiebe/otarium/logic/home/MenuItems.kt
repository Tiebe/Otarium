package nl.tiebe.otarium.logic.home

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

//TODO: fix this, too compose specific

/**
 * Information for displaying a menu item
 */
@Parcelize
class MenuItem(val string: String /* TODO: make this localized again */, val icon: () -> Unit, val iconSelected: () -> Unit): Parcelable