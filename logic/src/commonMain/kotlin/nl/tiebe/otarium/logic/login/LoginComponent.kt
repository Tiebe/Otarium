package nl.tiebe.otarium.logic.login

import nl.tiebe.otarium.logic.RootComponent

interface LoginComponent {
    val rootComponent: RootComponent

    suspend fun login()
}
