package nl.tiebe.otarium.wear.ui.home.timetable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TitleCard
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import nl.tiebe.otarium.logic.root.home.children.timetable.children.timetable.TimetableComponent
import nl.tiebe.otarium.magister.AgendaItemWithAbsence
import nl.tiebe.otarium.ui.utils.HtmlView
import nl.tiebe.otarium.utils.toFormattedStringTime
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun TimetableScreen(component: TimetableComponent) {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    component.refreshTimetable(today, today + DatePeriod(days = 31))

    val listState = rememberScalingLazyListState()
    val items = component.timetable.subscribeAsState().value.groupBy { it.start.toLocalDateTime(TimeZone.currentSystemDefault()).date }

    ScalingLazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
        for ((day, timetableItems) in items) {
            item { ListHeader {
                Text(day.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()) + " ${day.dayOfMonth}-${day.monthNumber}")
            } }
            for (item in timetableItems) item { TimetableItem(item = item) { /*component.parentComponent.navigate(TimetableRootComponent.Config.TimetablePopup(item))*/ } }
        }
    }
}

@Composable
fun TimetableItem(item: AgendaItemWithAbsence, onClick: () -> Unit) {
    TitleCard(onClick = onClick, title = {
        Text(text = item.agendaItem.subjects.firstOrNull()?.name?.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } ?: item.agendaItem.description ?: "No description")
    }, subtitle = {
        if (item.agendaItem.content != null) HtmlView(html = item.agendaItem.content!!, maxLines = 1, backgroundColor = -1, textColor = MaterialTheme.colorScheme.onSurface.toArgb())
    }, time = { Text("${item.start.toFormattedStringTime(false)} - ${item.end.toFormattedStringTime(false)}") })
}