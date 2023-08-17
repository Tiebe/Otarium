package nl.tiebe.otarium.logic.default.login

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.backhandler.BackCallback
import dev.tiebe.magisterapi.api.account.LoginFlow
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.logic.default.componentCoroutineScope
import nl.tiebe.otarium.logic.default.home.DefaultHomeComponent
import nl.tiebe.otarium.logic.root.RootComponent
import nl.tiebe.otarium.logic.root.login.LoginComponent
import nl.tiebe.otarium.logic.store.StoreHomeComponent
import nl.tiebe.otarium.magister.exchangeUrl
import nl.tiebe.otarium.magister.refreshGrades

class DefaultLoginComponent(val componentContext: ComponentContext, val navigateRootComponent: (RootComponent.ChildScreen) -> Unit): LoginComponent, ComponentContext by componentContext {
    override var loginUrl: LoginFlow.AuthURL = LoginFlow.createAuthURL()
    override val loading: MutableValue<Boolean> = MutableValue(false)

    override fun navigateToHomeScreen() {
        navigateRootComponent(
            RootComponent.ChildScreen.HomeChild(
            DefaultHomeComponent(
                componentContext,
                navigateRootComponent
            )
        ))
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
            account.refreshFolders()
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