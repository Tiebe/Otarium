package nl.tiebe.otarium.ui.home.timetable.item

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.logic.root.home.children.timetable.children.timetable.TimetableComponent
import nl.tiebe.otarium.magister.AgendaItemWithAbsence
import nl.tiebe.otarium.ui.utils.HtmlView
import nl.tiebe.otarium.utils.ui.getLocalizedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetablePopupTopAppBar(component: TimetableComponent, agendaItem: AgendaItemWithAbsence) {
    TopAppBar(
        title = { Text(agendaItem.agendaItem.description ?: "") },
        navigationIcon = {
            IconButton(onClick = { component.parentComponent.back() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        windowInsets = WindowInsets(0)
    )
}

@Composable
internal fun TimetableItemPopup(component: TimetableComponent, agendaItem: AgendaItemWithAbsence) {
    Box(Modifier.fillMaxSize().padding(top = 8.dp, start = 16.dp, end = 16.dp)) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            val startTime =
                agendaItem.agendaItem.start.substring(0, 26).toLocalDateTime()
                    .toInstant(TimeZone.UTC)
            val endTime = agendaItem.agendaItem.einde.substring(0, 26).toLocalDateTime()
                .toInstant(TimeZone.UTC)

            val localStartTime = startTime.toLocalDateTime(TimeZone.of("Europe/Amsterdam"))
            val localEndTime = endTime.toLocalDateTime(TimeZone.of("Europe/Amsterdam"))

            Text(
                agendaItem.agendaItem.description ?: "",
                style = MaterialTheme.typography.headlineSmall
            )
            val supportingPopupText = mutableListOf<String>()
            if (!agendaItem.agendaItem.location.isNullOrEmpty()) supportingPopupText.add(
                agendaItem.agendaItem.location!!
            )
            supportingPopupText.add(
                "${
                    localStartTime.hour.toString().padStart(2, '0')
                }:${localStartTime.minute.toString().padStart(2, '0')} - ${
                    localEndTime.hour.toString().padStart(2, '0')
                }:${localEndTime.minute.toString().padStart(2, '0')}"
            )


            Text(
                supportingPopupText.joinToString(" • "),
                style = MaterialTheme.typography.bodySmall,
            )

            Divider(Modifier.padding(top = 8.dp, bottom = 8.dp))

            if (agendaItem.absence != null) {
                Text(
                    getLocalizedString(MR.strings.absence),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    agendaItem.absence.description ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )
                Divider(Modifier.padding(top = 8.dp, bottom = 8.dp))
            }

            val text = (agendaItem.agendaItem.content ?: "")

            HtmlView(
                text
            )
        }
    }
}