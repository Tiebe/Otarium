package nl.tiebe.otarium.ui.login

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import dev.tiebe.magisterapi.api.account.LoginFlow
import io.ktor.http.*
import kotlinx.coroutines.launch
import nl.tiebe.otarium.ui.root.componentCoroutineScope

interface LoginComponent {
    var loginUrl: LoginFlow.AuthURL

    fun checkUrl(inputUrl: String): Boolean

    fun login(code: String, codeVerifier: String)

    fun addBackHandler(onBack: BackCallback)
}

class DefaultLoginComponent(val componentContext: ComponentContext): LoginComponent, ComponentContext by componentContext {
    override var loginUrl: LoginFlow.AuthURL = LoginFlow.createAuthURL()
    private val scope = componentCoroutineScope()

    override fun login(code: String, codeVerifier: String) {

    }

    override fun checkUrl(inputUrl: String): Boolean {
        val url = inputUrl.replace("#", "?")

        if (url.startsWith("m6loapp://oauth2redirect")) {
            val parameters = Url(url).parameters

            if (parameters.contains("error")) {
                println("Error: ${parameters["error"]}")

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

        return false
    }

    private var onBack: BackCallback? = null
    override fun addBackHandler(onBack: BackCallback) {
        if (this.onBack != null)
            backHandler.unregister(onBack)

        this.onBack = onBack
        backHandler.register(onBack)
    }

}