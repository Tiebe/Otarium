package nl.tiebe.otarium.ui.home.settings.utils

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf

internal abstract class SettingsItem(val parent: SettingsItem?) {
    private var openedSubItem = mutableStateOf<SettingsItem?>(null)

    @Composable
    abstract fun ItemContent()

    @Composable
    fun ItemPopup() {
        Crossfade(targetState = openedSubItem.value) { item ->
            when (item) {
                null -> ItemPopupContent()
                else -> item.ItemPopup()
            }
        }
    }

    @Composable
    protected open fun ItemPopupContent() {}

    protected fun openAsPopup() {
        parent?.openedSubItem?.value = this
    }

    fun closePopup(): Boolean {
        if (openedSubItem.value != null)
            openedSubItem.value = null
        else if (parent != null) {
            parent.openedSubItem.value = null
        } else {
            return false
        }
        return true
    }
}