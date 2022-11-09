package nl.tiebe.otarium.android.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.*
import nl.tiebe.otarium.ageOfConsent
import nl.tiebe.otarium.android.BuildConfig
import nl.tiebe.otarium.android.R
import nl.tiebe.otarium.android.ui.screen.agenda.AgendaScreen
import nl.tiebe.otarium.android.ui.screen.SettingsScreen
import nl.tiebe.otarium.showAds


@Composable
fun NavHostController(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(navController = navController, startDestination = "agenda", modifier = Modifier.padding(innerPadding)) {
        composable("agenda") {
            AgendaScreen()
        }
        composable("settings") {
            SettingsScreen()
        }
    }
}

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: @Composable () -> Unit) {
    object Agenda : Screen("agenda", R.string.agendaItem, { Icon(painterResource(R.drawable.ic_baseline_calendar_today_24), null) })
    object Settings : Screen("settings", R.string.settings_title, { Icon(Icons.Filled.Settings, null) })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(navController: NavHostController, modifier: Modifier) {
    val items = listOf(
        Screen.Agenda,
        Screen.Settings
    )
    Scaffold(
        bottomBar = {
            NavigationBar(modifier = modifier, contentColor = MaterialTheme.colorScheme.onPrimary, containerColor = MaterialTheme.colorScheme.primary) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = screen.icon,
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHostController(navController, innerPadding)
    }
}

var adsShown by mutableStateOf(showAds())

@Composable
fun Navigation() {
    val navController = rememberNavController()

    BottomBar(navController, Modifier.padding(bottom = if (adsShown) 50.dp else 0.dp))
    if (adsShown) {
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
    }
}