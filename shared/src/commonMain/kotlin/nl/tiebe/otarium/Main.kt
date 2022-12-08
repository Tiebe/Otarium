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
import com.russhwolf.settings.Settings
import nl.tiebe.otarium.magister.Tokens
import nl.tiebe.otarium.ui.navigation.Navigation
import nl.tiebe.otarium.ui.onboarding.OnBoarding
import nl.tiebe.otarium.ui.screen.LoginScreen
import nl.tiebe.otarium.ui.theme.OtariumTheme

val settings: Settings = Settings()

val darkmodeState = mutableStateOf(false)
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
internal fun Content() {
    setup()

    OtariumTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val openLoginScreen = remember { mutableStateOf(false) }
            if (openLoginScreen.value) MainActivityScreen()

            if (!isFinishedOnboarding()) {
                OnBoarding(onFinish = {
                    openLoginScreen.value = true
                    finishOnboarding()
                }, notifications = { setupNotifications() })
            } else MainActivityScreen()
        }
    }

    val darkMode = isSystemInDarkTheme()
    LaunchedEffect(key1 = Unit, block = {
        darkmodeState.value = darkMode
    })
}


@Composable
internal fun MainActivityScreen() {
    if (storeBypass()) {
        Navigation(); return
    }
    val openMainScreen = remember { mutableStateOf(false) }

    if (openMainScreen.value) Navigation()

    if (Tokens.getPastTokens() == null) {
        LoginScreen(onLogin = {
            openMainScreen.value = true
        })
    } else {
        Navigation()
    }
}

expect fun setupNotifications()