package nl.tiebe.otarium.logic.magister.login

import com.arkivanov.decompose.ComponentContext
import dev.tiebe.magisterapi.api.account.LoginFlow
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nl.tiebe.otarium.logic.RootComponent
import nl.tiebe.otarium.logic.componentCoroutineScope
import nl.tiebe.otarium.logic.data.Data
import nl.tiebe.otarium.logic.magister.home.DefaultHomeComponent
import nl.tiebe.otarium.magister.exchangeUrl
import nl.tiebe.otarium.magister.refreshGrades
import nl.tiebe.otarium.logic.login.LoginComponent

/**
 * The default implementation of the [LoginComponent] interface.
 *
 * @param componentContext The [ComponentContext] of this component.
 * @param rootComponent The [RootComponent] of this component.
 */
class DefaultLoginComponent(val componentContext: ComponentContext,
                            override val rootComponent: RootComponent
): LoginComponent, ComponentContext by componentContext {
    private var loginUrl: LoginFlow.AuthURL = LoginFlow.createAuthURL()

    override var startingUrl: String = loginUrl.url

    override fun regenerateStartingUrl() {
        loginUrl = LoginFlow.createAuthURL()
        startingUrl = loginUrl.url
    }


    val scope: CoroutineScope = componentCoroutineScope()

    private suspend fun login(code: String, codeVerifier: String): Boolean {
        try {
            //TODO: loading.value = true
            val account = exchangeUrl(code, codeVerifier)

            if (Data.accounts.find { acc -> acc.profileInfo.person.id == account.profileInfo.person.id } == null) {
                Data.accounts =
                    Data.accounts.toMutableList().apply { add(account) }
            }

            account.refreshGrades()
            rootComponent.currentScreen.value =
                RootComponent.ChildScreen.HomeChild(DefaultHomeComponent(rootComponent, rootComponent))

            return true
        } catch (e: Exception) {
            e.printStackTrace()
            //TODO: loading.value = false
            return false
        }
    }

    override fun urlChangeCallback(newUrl: String): Boolean {
        val url = newUrl.replace("#", "?")

        if (url.startsWith("m6loapp://oauth2redirect")) {
            val parameters = Url(url).parameters

            if (parameters.contains("error")) {
                loginUrl = LoginFlow.createAuthURL()

                return false
            } else {
                val code = parameters["code"] ?: return false

                scope.launch {
                    login(code, loginUrl.codeVerifier)
                }

                return true
            }
        }

        return true
    }
}