package nl.tiebe.otarium.android.ui

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
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
import nl.tiebe.otarium.android.ui.screen.MainScreen
import nl.tiebe.otarium.android.ui.screen.SettingsScreen
import nl.tiebe.otarium.showAds


@Composable
fun NavHostController(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(navController = navController, startDestination = "main", modifier = Modifier.padding(innerPadding)) {
        composable("main") {
            MainScreen()
        }
        composable("settings") {
            SettingsScreen()
        }
    }
}

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Main : Screen("main", R.string.mainBarItem)
    //object Item2 : Screen("login", R.string.bar2)
    object Settings : Screen("settings", R.string.settings_title)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(navController: NavHostController, modifier: Modifier = Modifier) {
    val items = listOf(
        Screen.Main,
        //Screen.Item2,
        Screen.Settings
    )
    Scaffold(
        bottomBar = {
            BottomNavigation(modifier = modifier, contentColor = MaterialTheme.colorScheme.onPrimary, backgroundColor = MaterialTheme.colorScheme.primary) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
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



@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()

    BottomBar(navController, Modifier.padding(bottom = if (showAds()) 50.dp else 0.dp))

    if (showAds()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = { context ->
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