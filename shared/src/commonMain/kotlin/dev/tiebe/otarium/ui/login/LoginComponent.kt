package dev.tiebe.otarium.ui.login

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import dev.tiebe.magisterapi.api.account.LoginFlow
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import dev.tiebe.otarium.Data
import dev.tiebe.otarium.magister.exchangeUrl
import dev.tiebe.otarium.magister.refreshGrades
import dev.tiebe.otarium.store.component.home.StoreHomeComponent
import dev.tiebe.otarium.ui.home.DefaultHomeComponent
import dev.tiebe.otarium.ui.root.RootComponent
import dev.tiebe.otarium.ui.root.componentCoroutineScope

interface LoginComponent {
    var loginUrl: LoginFlow.AuthURL
    val loading: Value<Boolean>

    fun navigateToHomeScreen()

    fun bypassLogin()

    fun checkUrl(inputUrl: String): Boolean

    suspend fun login(code: String, codeVerifier: String)

    fun addBackHandler(onBack: BackCallback)
    val scope: CoroutineScope
}

class DefaultLoginComponent(val componentContext: ComponentContext, val navigateRootComponent: (RootComponent.ChildScreen) -> Unit): LoginComponent, ComponentContext by componentContext {
    override var loginUrl: LoginFlow.AuthURL = LoginFlow.createAuthURL()
    override val loading: MutableValue<Boolean> = MutableValue(false)

    override fun navigateToHomeScreen() {
        navigateRootComponent(RootComponent.ChildScreen.HomeChild(DefaultHomeComponent(componentContext, navigateRootComponent)))
    }

    override fun bypassLogin() {
        scope.launch {
            Data.storeLoginBypass = true
            navigateRootComponent(RootComponent.ChildScreen.HomeChild(StoreHomeComponent(componentContext, navigateRootComponent)))
        }
    }

    override val scope: CoroutineScope = componentCoroutineScope()

    override suspend fun login(code: String, codeVerifier: String) {
        try {
            loading.value = true
            val account = exchangeUrl(code, codeVerifier)

            if (Data.accounts.find { acc -> acc.profileInfo.person.id == account.profileInfo.person.id } == null) {
                Data.accounts =
                    Data.accounts.toMutableList().apply { add(account) }
            }

            account.refreshGrades()
            navigateToHomeScreen()
        } catch (e: Exception) {
            e.printStackTrace()
            loading.value = false
        }
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
            backHandler.unregister(this.onBack!!)

        this.onBack = onBack
        backHandler.register(onBack)
    }

}