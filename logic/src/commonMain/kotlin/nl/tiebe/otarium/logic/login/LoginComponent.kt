package nl.tiebe.otarium.logic.login

import nl.tiebe.otarium.logic.RootComponent

interface LoginComponent {
    val rootComponent: RootComponent

    var startingUrl: String


    fun urlChangeCallback(newUrl: String): Boolean
    fun regenerateStartingUrl()
}