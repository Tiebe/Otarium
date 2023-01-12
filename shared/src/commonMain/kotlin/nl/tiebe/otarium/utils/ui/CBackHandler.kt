package nl.tiebe.otarium.utils.ui

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback

class CBackHandler(componentContext: ComponentContext, onBack: () -> Unit): ComponentContext by componentContext {
    private val backCallback = BackCallback { onBack() }

    init {
        backHandler.register(backCallback)
    }

    fun enableBackCallback(state: Boolean) {
        backCallback.isEnabled = state
    }
}