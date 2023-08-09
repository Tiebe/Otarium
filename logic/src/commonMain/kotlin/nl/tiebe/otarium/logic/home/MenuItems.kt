package nl.tiebe.otarium.logic.home

import androidx.compose.runtime.Composable
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

//TODO: fix this, too compose specific

/**
 * Information for displaying a menu item
 */
@Parcelize
sealed class MenuItem(val string: String /* TODO: make this localized again */, val icon: @Composable () -> Unit, val iconSelected: @Composable () -> Unit): Parcelable