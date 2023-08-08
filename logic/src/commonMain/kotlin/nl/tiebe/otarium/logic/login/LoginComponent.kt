package nl.tiebe.otarium.logic.login

import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import dev.tiebe.magisterapi.api.account.LoginFlow
import kotlinx.coroutines.CoroutineScope

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
