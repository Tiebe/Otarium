package nl.tiebe.otarium.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.StringResource
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.showAds
import nl.tiebe.otarium.ui.screen.SettingsScreen
import nl.tiebe.otarium.ui.screen.agenda.AgendaScreen
import nl.tiebe.otarium.ui.screen.grades.GradeScreen

sealed class Screen(val resourceId: StringResource, val icon: @Composable () -> Unit) {
    object Agenda : Screen(MR.strings.agendaItem, { /*Icon(painterResource(R.drawable.ic_baseline_calendar_today_24), "Timetable")*/ })
    object Grades : Screen(MR.strings.gradesItem, { /*Icon(painterResource(R.drawable.ic_baseline_looks_10_24), "Grades")*/ })
    object Settings : Screen(MR.strings.settings_title, { /*Icon(Icons.Filled.Settings, "Settings")*/ })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(screenState: MutableState<Screen>, modifier: Modifier) {
    val items = listOf(
        Screen.Agenda,
        Screen.Grades,
        Screen.Settings
    )
    //TODO: Fix navigation bar
/*    Scaffold(
        bottomBar = {
            NavigationBar(modifier = modifier, contentColor = MaterialTheme.colorScheme.onPrimary, containerColor = MaterialTheme.colorScheme.primary) {
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = screen.icon,
                        label = { Text(getLocalizedString(screen.resourceId)) },
                        selected = screenState.value == screen,
                        onClick = {
                            screenState.value = screen
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // TODO: maybe put content here
    }*/
}

var adsShown by mutableStateOf(showAds())

@Composable
fun Navigation() {
    val screenState = remember { mutableStateOf<Screen>(Screen.Agenda) }

    when (val screen = screenState.value) {
        is Screen.Agenda -> AgendaScreen()
        is Screen.Grades -> GradeScreen()
        is Screen.Settings -> SettingsScreen()
    }

    BottomBar(screenState, Modifier.padding(bottom = if (adsShown) 50.dp else 0.dp))


    /*if (adsShown) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = { context ->
                    MobileAds.initialize(context) {}

                    AdView(context).apply {
                        adUnitId =
                            if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/6300978111" // test ads
                            else "ca-app-pub-1684141915170982/7736101438" // prod ads
                        setAdSize(AdSize.BANNER)

                        val requestConfiguration = MobileAds.getRequestConfiguration()
                            .toBuilder()
                            .setTagForChildDirectedTreatment(ageOfConsent().compareTo(false))
                            .setTagForUnderAgeOfConsent(ageOfConsent().compareTo(false))
                            .build()
                        MobileAds.setRequestConfiguration(requestConfiguration)

                        loadAd(AdRequest.Builder().build())
                    }
                }
            )
        }
    }*/
}