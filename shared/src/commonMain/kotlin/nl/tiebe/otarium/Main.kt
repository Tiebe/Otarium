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
import nl.tiebe.otarium.Data.Onboarding.finishOnboarding
import nl.tiebe.otarium.Data.Onboarding.isFinishedOnboarding
import nl.tiebe.otarium.Data.Onboarding.storeBypass
import nl.tiebe.otarium.magister.Tokens
import nl.tiebe.otarium.magister.exchangeUrl
import nl.tiebe.otarium.ui.navigation.Navigation
import nl.tiebe.otarium.ui.onboarding.OnBoarding
import nl.tiebe.otarium.ui.screen.LoginScreen
import nl.tiebe.otarium.ui.theme.OtariumTheme
import nl.tiebe.otarium.utils.refreshGrades

val settings: Settings = Settings()

val darkModeState = mutableStateOf(false)
val safeAreaState = mutableStateOf(PaddingValues())

fun setup() {
    val version = settings.getInt("version", 0)

    //REMINDER: UPDATE VERSION CODE IN BOTH THE ANDROID MODULE AND SHARED BUILDKONFIG MODULE!!

    if (version <= 14) {
        settings.remove("agenda")
    }

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

            if (!isFinishedOnboarding()) {
                OnBoarding(onFinish = {
                    openLoginScreen.value = true
                    finishOnboarding()
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
    if (storeBypass()) {
        Navigation(componentContext); return
    }
    val openMainScreen = remember { mutableStateOf(false) }

    if (openMainScreen.value) Navigation(componentContext)
    else if (Tokens.getSavedMagisterTokens() == null) {
        LoginScreen(componentContext, onLogin = {
            if (storeBypass()) openMainScreen.value = true

            else runBlocking {
                launch {
                    exchangeUrl(it)

                    openMainScreen.value = true
                    refreshGrades()
                }
            }
        })
    } else {
        Navigation(componentContext)
    }
}

expect fun setupNotifications()