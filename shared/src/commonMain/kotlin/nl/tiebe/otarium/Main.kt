package nl.tiebe.otarium

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.russhwolf.settings.Settings
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.tiebe.otarium.magister.exchangeUrl
import nl.tiebe.otarium.magister.refreshGrades
import nl.tiebe.otarium.ui.navigation.Navigation
import nl.tiebe.otarium.ui.onboarding.OnBoarding
import nl.tiebe.otarium.ui.screen.LoginScreen
import nl.tiebe.otarium.ui.theme.OtariumTheme
import nl.tiebe.otarium.utils.runVersionCheck

val settings: Settings = Settings()

val darkModeState = mutableStateOf(false)
val safeAreaState = mutableStateOf(PaddingValues())

fun setup() {
    val oldVersion = settings.getInt("version", 1000)

    //REMINDER: UPDATE VERSION CODE IN BOTH THE ANDROID MODULE AND SHARED BUILDKONFIG MODULE!!
    runVersionCheck(oldVersion)
    settings.putInt("version", BuildKonfig.versionCode)
}

@Composable
internal fun Content(componentContext: ComponentContext) {
    setup()

    OtariumTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val openLoginScreen = remember { mutableStateOf(false) }
            if (openLoginScreen.value) MainActivityScreen(componentContext)

            if (!Data.finishedOnboarding) {
                OnBoarding(onFinish = {
                    openLoginScreen.value = true
                    Data.finishedOnboarding = true
                }, notifications = { setupNotifications() })
            } else MainActivityScreen(componentContext)
        }
    }

    val darkMode = isSystemInDarkTheme()
    LaunchedEffect(key1 = darkMode, block = {
        darkModeState.value = darkMode
    })
}


@Composable
internal fun MainActivityScreen(componentContext: ComponentContext) {
    if (Data.storeLoginBypass) {
        Navigation(componentContext) {}; return
    }
    val openMainScreen = remember { mutableStateOf(false) }
    val openLoginScreen = remember { mutableStateOf(false) }

    if (openLoginScreen.value) LoginScreen(componentContext, onLogin = {
        if (Data.storeLoginBypass) openMainScreen.value = true

        else runBlocking {
            launch {
                val account = exchangeUrl(it)

                if (Data.accounts.find { acc -> acc.profileInfo.person.id == account.profileInfo.person.id } != null) {
                    Data.accounts = Data.accounts.toMutableList().apply { add(account) }
                }

                openLoginScreen.value = false
                openMainScreen.value = true

                account.refreshGrades()
            }
        }
    })

    else if (openMainScreen.value) Navigation(componentContext) { openLoginScreen.value = true }
    else if (Data.accounts.isEmpty()) {
        openLoginScreen.value = true
    } else {
        Navigation(componentContext) {
            openLoginScreen.value = true
        }
    }
}

expect fun setupNotifications()