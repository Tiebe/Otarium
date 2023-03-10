package nl.tiebe.otarium

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.russhwolf.settings.Settings
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.tiebe.otarium.magister.exchangeUrl
import nl.tiebe.otarium.magister.refreshGrades
import nl.tiebe.otarium.oldui.navigation.Navigation
import nl.tiebe.otarium.oldui.navigation.Screen
import nl.tiebe.otarium.oldui.screen.LoginScreen
import nl.tiebe.otarium.oldui.theme.OtariumTheme
import nl.tiebe.otarium.ui.home.HomeScreen
import nl.tiebe.otarium.ui.root.DefaultRootComponent
import nl.tiebe.otarium.ui.root.RootComponent
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
    Content(component = DefaultRootComponent(componentContext))
}

@Composable
internal fun Content(component: RootComponent) {
    setup()
    darkModeState = mutableStateOf(isSystemInDarkTheme())
    val currentScreen by component.currentScreen.subscribeAsState()

    OtariumTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            when (val screen = currentScreen) {
                is RootComponent.ChildScreen.HomeChild -> HomeScreen(screen.component)
                is RootComponent.ChildScreen.LoginChild -> LoginScreen(screen.component.componentContext) {
                    runBlocking {
                        launch {
                            val account = exchangeUrl(it)

                            if (Data.accounts.find { acc -> acc.profileInfo.person.id == account.profileInfo.person.id } == null) {
                                Data.accounts = Data.accounts.toMutableList().apply { add(account) }
                            }

                            account.refreshGrades()
                        }
                    }
                }
            }
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

expect fun closeApp()