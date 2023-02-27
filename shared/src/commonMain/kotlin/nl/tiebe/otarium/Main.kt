package nl.tiebe.otarium

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.russhwolf.settings.Settings
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.tiebe.otarium.magister.exchangeUrl
import nl.tiebe.otarium.magister.refreshGrades
import nl.tiebe.otarium.ui.navigation.Navigation
import nl.tiebe.otarium.ui.navigation.Screen
import nl.tiebe.otarium.ui.onboarding.OnBoarding
import nl.tiebe.otarium.ui.screen.LoginScreen
import nl.tiebe.otarium.ui.theme.OtariumTheme
import nl.tiebe.otarium.utils.runVersionCheck

val settings: Settings = Settings()

lateinit var darkModeState: MutableState<Boolean>
val safeAreaState = mutableStateOf(PaddingValues())

fun setup() {
    val oldVersion = settings.getInt("version", 1000)

    runVersionCheck(oldVersion)

    settings.putInt("version", BuildKonfig.versionCode)

}

@Composable
internal fun Content(componentContext: ComponentContext) {
    setup()
    darkModeState = mutableStateOf(isSystemInDarkTheme())

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
}


@Composable
internal fun MainActivityScreen(componentContext: ComponentContext) {
    val navigation = remember { StackNavigation<Screen>() }

    val childStack = remember {
        componentContext.childStack(
            source = navigation,
            initialStack = { listOf(Screen.Agenda) },
            handleBackButton = true,
            childFactory = { _, childComponentContext -> childComponentContext },
        )
    }


    if (Data.storeLoginBypass) {
        Navigation(childStack, navigation, componentContext) {}; return
    }

    val openMainScreen = remember { mutableStateOf(true) }
    val openLoginScreen = remember { mutableStateOf(false) }

    if (Data.accounts.isEmpty()) {
        openLoginScreen.value = true
    } else if (openMainScreen.value) {
        Navigation(childStack, navigation, componentContext) {
            openLoginScreen.value = true
        }
    }

    if (openLoginScreen.value) LoginScreen(componentContext, onLogin = {
        if (Data.storeLoginBypass) openMainScreen.value = true

        else runBlocking {
            launch {
                val account = exchangeUrl(it)

                if (Data.accounts.find { acc -> acc.profileInfo.person.id == account.profileInfo.person.id } == null) {
                    Data.accounts = Data.accounts.toMutableList().apply { add(account) }
                }

                account.refreshGrades()

                openLoginScreen.value = false
                openMainScreen.value = true
            }
        }
    })
}

expect fun setupNotifications()