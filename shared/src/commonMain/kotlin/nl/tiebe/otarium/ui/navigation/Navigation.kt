package nl.tiebe.otarium.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import dev.icerock.moko.resources.StringResource
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.showAds
import nl.tiebe.otarium.ui.icons.CalendarTodayIcon
import nl.tiebe.otarium.ui.icons.Looks10Icon
import nl.tiebe.otarium.ui.icons.SettingsIcon
import nl.tiebe.otarium.ui.screen.agenda.AgendaScreen
import nl.tiebe.otarium.ui.screen.grades.GradeScreen
import nl.tiebe.otarium.ui.screen.settings.SettingsScreen

sealed class Screen(val resourceId: StringResource, val icon: @Composable () -> Unit) : Parcelable {
    @Parcelize
    object Agenda: Screen(MR.strings.agendaItem, { Icon(CalendarTodayIcon, "Timetable") })

    @Parcelize
    object Grades: Screen(MR.strings.gradesItem, { Icon(Looks10Icon, "Grades") })

    @Parcelize
    object Settings: Screen(MR.strings.settings_title, { Icon(SettingsIcon, "Settings") })
}

/*sealed class Screen(val route: String, val resourceId: StringResource, val icon: @Composable () -> Unit) {
    object Agenda : Screen("/agenda", MR.strings.agendaItem, { Icon(CalendarTodayIcon, "Timetable") })
    object Grades : Screen("/grades", MR.strings.gradesItem, { Icon(Looks10Icon, "Grades") })
    object Settings : Screen("/settings", MR.strings.settings_title, { Icon(SettingsIcon, "Settings") })
}*/

var adsShown by mutableStateOf(showAds())

@Composable
internal fun NavHostController(navController: Navigator, innerPadding: PaddingValues) {
    NavHost(navigator = navController, initialRoute = "/agenda", modifier = Modifier.padding(innerPadding)) {
        scene("/agenda") {
            AgendaScreen()
        }
        scene("/grades", deepLinks = listOf("https://otarium.groosman.nl/grades")) {
            GradeScreen()
        }
        scene("/settings") {
            SettingsScreen()
        }
    }
}


@Composable
internal fun Navigation() {
    val navigation = remember { StackNavigation<Screen>() }

    BottomBar(navigation, Modifier.padding(bottom = if (adsShown) 50.dp else 0.dp))

    if (adsShown) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Ads()
        }
    }
}

@Composable
internal expect fun Ads()