package nl.tiebe.otarium.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.StringResource
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.showAds
import nl.tiebe.otarium.ui.icons.CalendarTodayIcon
import nl.tiebe.otarium.ui.icons.Looks10Icon
import nl.tiebe.otarium.ui.icons.SettingsIcon
import nl.tiebe.otarium.ui.screen.SettingsScreen
import nl.tiebe.otarium.ui.screen.agenda.AgendaScreen
import nl.tiebe.otarium.ui.screen.grades.GradeScreen
import nl.tiebe.otarium.utils.getLocalizedString

sealed class Screen(val resourceId: StringResource, val icon: @Composable () -> Unit) {
    object Agenda : Screen(MR.strings.agendaItem, { Icon(CalendarTodayIcon, "Timetable") })
    object Grades : Screen(MR.strings.gradesItem, { Icon(Looks10Icon, "Grades") })
    object Settings : Screen(MR.strings.settings_title, { Icon(SettingsIcon, "Settings") })
}

var adsShown by mutableStateOf(showAds())

@Composable
fun Navigation() {
    val screenState = remember { mutableStateOf<Screen>(Screen.Agenda) }

    BottomBar(screenState, Modifier.padding(bottom = if (adsShown) 50.dp else 0.dp))


    if (adsShown) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Ads()
        }
    }
}

@Composable
expect fun Ads()