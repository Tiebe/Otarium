package nl.tiebe.otarium.ui.login

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import dev.tiebe.magisterapi.api.account.LoginFlow
import io.ktor.http.*
import kotlinx.coroutines.launch
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.magister.exchangeUrl
import nl.tiebe.otarium.magister.refreshGrades
import nl.tiebe.otarium.ui.home.DefaultHomeComponent
import nl.tiebe.otarium.ui.root.RootComponent
import nl.tiebe.otarium.ui.root.componentCoroutineScope

interface LoginComponent {
    var loginUrl: LoginFlow.AuthURL

    fun navigateToHomeScreen()

    fun checkUrl(inputUrl: String): Boolean

    suspend fun login(code: String, codeVerifier: String)

    fun addBackHandler(onBack: BackCallback)
}

class DefaultLoginComponent(val componentContext: ComponentContext, val navigateRootComponent: (RootComponent.ChildScreen) -> Unit): LoginComponent, ComponentContext by componentContext {
    override var loginUrl: LoginFlow.AuthURL = LoginFlow.createAuthURL()

    override fun navigateToHomeScreen() {
        navigateRootComponent(RootComponent.ChildScreen.HomeChild(DefaultHomeComponent(componentContext, navigateRootComponent)))
    }

    private val scope = componentCoroutineScope()

    override suspend fun login(code: String, codeVerifier: String) {
        val account = exchangeUrl(code, codeVerifier)

        if (Data.accounts.find { acc -> acc.profileInfo.person.id == account.profileInfo.person.id } == null) {
            Data.accounts =
                Data.accounts.toMutableList().apply { add(account) }
        }

        account.refreshGrades()
        navigateToHomeScreen()
    }

    override fun checkUrl(inputUrl: String): Boolean {
        val url = inputUrl.replace("#", "?")

        if (url.startsWith("m6loapp://oauth2redirect")) {
            val parameters = Url(url).parameters

            if (parameters.contains("error")) {
                loginUrl = LoginFlow.createAuthURL()

                return false
            } else {
                val code = parameters["code"] ?: return false

                scope.launch {
                    onBack?.let { backHandler.unregister(it) }
                    login(code, loginUrl.codeVerifier)
                }

                return true
            }
        }

        return true
    }

    private var onBack: BackCallback? = null
    override fun addBackHandler(onBack: BackCallback) {
        if (this.onBack != null)
            backHandler.unregister(onBack)

        this.onBack = onBack
        backHandler.register(onBack)
    }

}